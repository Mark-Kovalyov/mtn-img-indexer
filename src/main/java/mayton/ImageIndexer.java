package mayton;

import mayton.html.HtmlWriter;
import mayton.html.HtmlWriterSimple;
import mayton.html.XHtmlWriter;
import org.apache.commons.cli.*;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.profiler.Profiler;
import org.slf4j.profiler.TimeInstrument;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static mayton.FileUtils.extractLastPathElement;
import static mayton.FileUtils.trimExtension;
import static mayton.ImageUtils.*;

// mayton : Jul-30, 2021 - Initial commit
// mayton : 1-Aug, 2021 - changes
// mayton : 4-Aug, 2021 - changes

public class ImageIndexer {

    private StopWatch readImageStopWatcher = StopWatch.create();
    private StopWatch resizeImageStopWatcher = StopWatch.create();
    private StopWatch avgColorImageStopWatcher = StopWatch.create();

    public static final String MINI_SUFFIX = "-mini";
    public static final String INDEX_HTML = "index.html";
    public static final Pattern JPEG_MINI_EXTENSION = Pattern.compile(".+?(?<suffix>-(mini|bars|gradient))?\\.(?<extension>jpg|jfif|jpe|jpeg)$", Pattern.CASE_INSENSITIVE);

    public static Logger logger = LoggerFactory.getLogger(ImageIndexer.class);
    private final String sourceDir;
    private int targetHeightSize;
    private String rootFolderName;

    private CommandLine commandLine;

    public ImageIndexer(CommandLine line) {
        this.commandLine = line;
        sourceDir = commandLine.getOptionValue("s");
        targetHeightSize = commandLine.hasOption("h") ? Integer.parseInt(commandLine.getOptionValue("h")) : 128;
        rootFolderName = commandLine.hasOption("r") ? commandLine.getOptionValue("r") : "";
    }

    public static Options createOptions() {
        return new Options()
                .addRequiredOption("s", "source",  true, "Source jpeg files folder")
                .addOption("h", "thumnailheight",  true, "Thumbnail height size in pixels (default = 256)")
                .addOption("r", "rootfoldername",  true, "Root folder name. For ex: 'My Photos'");
    }

    public static void printHelp() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("java -jar image-indexer.jar", createOptions(), true);
    }

    public void processFile(String localFolderPrefix, File file, HtmlWriter htmlWriter) throws IOException {
        String fileName = extractLastPathElement(file.getName());
        Matcher miniMatcher = JPEG_MINI_EXTENSION.matcher(fileName);
        if (miniMatcher.matches() && miniMatcher.group("suffix") == null) {
            String extension = miniMatcher.group("extension");
            logger.info("process jpeg file {}", file);
            readImageStopWatcher.resume();
            BufferedImage image = ImageIO.read(file);
            readImageStopWatcher.suspend();
            if (image == null) {
                logger.warn("Something going wrong with file {}. Unable to read image.", file);
            } else {
                int x = image.getWidth();
                int y = image.getHeight();
                // TODO: fix non-accurate calculation of resize in pixels
                double scale = (double) targetHeightSize / y;
                resizeImageStopWatcher.resume();
                int thumbnailX = (int) (x * scale);
                int thumbnailY = (int) (y * scale);
                BufferedImage thumbnail = resizeImage(image, thumbnailX, thumbnailY);
                resizeImageStopWatcher.suspend();
                String miniPath = trimExtension(file.getAbsoluteFile().toString()) + MINI_SUFFIX + "." + extension;
                try (OutputStream outputStream = new FileOutputStream(miniPath)) {
                    ImageIO.write(thumbnail, "JPEG", outputStream);
                }
                String src = trimExtension(fileName) + MINI_SUFFIX + "." + extension;
                htmlWriter.beginAnchor(fileName);
                htmlWriter.writeImg("", src, null, x, y, "");
                htmlWriter.endAnchor();
            }
        }
    }

    public void routeFolder(String rootName, String htmlLocalFolder, File folder, HtmlWriter parentHtmlWriter) throws Exception {
        String title = rootName == null ? folder.getName() : rootName;
        parentHtmlWriter.writeH1(title);
        for(File node : folder.listFiles()) {
            if (node.isDirectory()) {
                String nodeName = node.getName();
                parentHtmlWriter.emptyAnchor(node.getName() + "/" + INDEX_HTML, "[" + node.getName() + "]");
                parentHtmlWriter.endAnchor();
                parentHtmlWriter.lineBreak();
                HtmlWriter currentHtmlWriter = new XHtmlWriter(new FileWriter(node.getAbsoluteFile() + "/" + INDEX_HTML), title, "#FF000000", this.targetHeightSize);
                routeFolder(null, htmlLocalFolder.isBlank() ? nodeName : htmlLocalFolder + "/" + nodeName,
                        node, currentHtmlWriter);
                currentHtmlWriter.close();
            }
        }
        parentHtmlWriter.beginDiv();
        for(File node : folder.listFiles()) {
            if (!(node.isDirectory())) {
                processFile(htmlLocalFolder, node, parentHtmlWriter);
            }
        }
        parentHtmlWriter.endDiv();
        parentHtmlWriter.close();
    }


    public static void main(String[] args) throws Exception {
        CommandLineParser parser = new DefaultParser();
        Options options = createOptions();
        if (args.length == 0) {
            printHelp();
        } else {
            CommandLine line = parser.parse(options, args);
            process(line);
        }
    }

    private static void process(CommandLine line) throws Exception {
        logger.info("Start");
        ImageIndexer imageIndexer = new ImageIndexer(line);
        imageIndexer.go();
        logger.info("Finish");
    }

    private void go() throws Exception {

        avgColorImageStopWatcher.start();
        avgColorImageStopWatcher.suspend();
        readImageStopWatcher.start();
        readImageStopWatcher.suspend();
        resizeImageStopWatcher.start();
        resizeImageStopWatcher.suspend();

        Profiler profiler = new Profiler("image-indexer");
        profiler.setLogger(logger);
        profiler.start("indexing");
        // TODO: Get rid of writer+strategy pair
        HtmlWriter htmlWriter = new XHtmlWriter(new FileWriter(sourceDir + "/" + INDEX_HTML), rootFolderName, "#FF000000", targetHeightSize);
        routeFolder(rootFolderName, rootFolderName, new File(sourceDir), htmlWriter);
        htmlWriter.close();
        TimeInstrument timeInstrument = profiler.stop();
        timeInstrument.log();

        avgColorImageStopWatcher.stop();
        readImageStopWatcher.stop();
        resizeImageStopWatcher.stop();

        logger.info("Statistics: ");
        logger.info("readImage elapsed time          : {} ms", readImageStopWatcher.getTime(TimeUnit.MILLISECONDS));
        logger.info("resize image elapsed time       : {} ms", resizeImageStopWatcher.getTime(TimeUnit.MILLISECONDS));
        logger.info("write thumbnails elapsed time   : ? ms");
    }
}
