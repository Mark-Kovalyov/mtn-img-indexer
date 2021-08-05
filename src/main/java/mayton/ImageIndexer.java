package mayton;

import mayton.html.HtmlWriter;
import org.apache.commons.cli.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.apache.commons.math3.ml.clustering.CentroidCluster;
import org.apache.commons.math3.ml.clustering.Clusterable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.profiler.Profiler;
import org.slf4j.profiler.TimeInstrument;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static mayton.ImageUtils.*;

// mayton : Jul-30, 2021 - Initial commit
// mayton : 1-Aug, 2021 - changes
// mayton : 4-Aug, 2021 - changes

public class ImageIndexer {

    private StopWatch readImageStopWatcher = StopWatch.create();
    private StopWatch resizeImageStopWatcher = StopWatch.create();
    private StopWatch avgColorImageStopWatcher = StopWatch.create();

    public static final String MINI_SUFFIX = "-mini.jpeg";
    public static final String INDEX_HTML = "index.html";
    public static final Pattern JPEG_MINI_EXTENSION = Pattern.compile(".+?(?<suffix>-(mini|bars|gradient))?\\.(?<extension>jpg|jfif|jpe|jpeg)$", Pattern.CASE_INSENSITIVE);

    // TODO: Check for all possible
    public static String SEPARATOR = System.getProperty("file.separator");

    public static Logger logger = LoggerFactory.getLogger(ImageIndexer.class);
    private final String sourceDir;

    private int targetHeightSize;

    private CommandLine commandLine;

    public ImageIndexer(CommandLine line) {
        this.commandLine = line;
        sourceDir = commandLine.getOptionValue("s");
        targetHeightSize = commandLine.hasOption("z") ? Integer.parseInt(commandLine.getOptionValue("z")) : 256;
    }

    public static Options createOptions() {
        return new Options()
                .addRequiredOption("s", "source", true, "Source jpeg files folder")
                .addOption("z", "size", true, "Downscale to height size in px (default 256)");
    }

    public static void printHelp() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("java -jar image-indexer.jar", createOptions(), true);
    }

    public void processFile(String localFolderPrefix, File file, HtmlWriter htmlWriter) throws IOException {
        String fileName = file.getName();
        Matcher miniMatcher = JPEG_MINI_EXTENSION.matcher(fileName);
        if (miniMatcher.matches() && miniMatcher.group("suffix") == null) {
            logger.info("process jpeg file {}", file);
            readImageStopWatcher.resume();
            BufferedImage image = ImageIO.read(file);
            readImageStopWatcher.suspend();
            int x = image.getWidth();
            int y = image.getHeight();
            // TODO: fix non-accurate calculation of resize in pixels
            double scale = (double) targetHeightSize / y;
            resizeImageStopWatcher.resume();
            int thumbnailX = (int) (x * scale);
            int thumbnailY = (int) (y * scale);
            BufferedImage thumbnail = resizeImage(image, thumbnailX, thumbnailY);
            resizeImageStopWatcher.suspend();
            try(OutputStream outputStream = new FileOutputStream(trimExtension(file.getAbsoluteFile().toString()) + MINI_SUFFIX)) {
                ImageIO.write(thumbnail, "JPEG", outputStream);
            }
            htmlWriter.writeH1(fileName);
            avgColorImageStopWatcher.resume();
            String avgColor = tripleToHex(averageColor(image));
            avgColorImageStopWatcher.suspend();
            htmlWriter.writeImg(trimExtension(fileName) + MINI_SUFFIX,
                    String.format("background-color:%s;", avgColor),
                    thumbnailX,
                    thumbnailY);

        }
    }


    public static String trimExtension(String absoluteFile) {
        int index = absoluteFile.lastIndexOf('.');
        return index > 1 ? absoluteFile.substring(0, index) : absoluteFile;
    }

    public static String extractLastPathElement(String path) {
        int index = path.lastIndexOf(SEPARATOR);
        return index >= 0 ? path.substring(index + 1) : path;
    }

    public void routeFolder(String htmlLocalFolder, File folder, HtmlWriter parentHtmlWriter) throws Exception {
        for(File node : folder.listFiles()) {
            if (node.isDirectory() && !node.getName().equals("css")) {
                String nodeName = node.getName();
                parentHtmlWriter.writeAnchor(node.getName() + "/" + INDEX_HTML, "[" + node.getName() + "]");
                parentHtmlWriter.writeParagraph();
                HtmlWriter currentHtmlWriter = new HtmlWriter(new FileWriter(node.getAbsoluteFile() + "/" + INDEX_HTML));
                routeFolder(htmlLocalFolder.isBlank() ? nodeName : htmlLocalFolder + "/" + nodeName,
                        node, currentHtmlWriter);
                currentHtmlWriter.close();
            }
        }

        for(File node : folder.listFiles()) {
            if (node.isDirectory() && !node.getName().equals("css")) {
                //
            } else {
                processFile(htmlLocalFolder, node, parentHtmlWriter);
            }
        }
    }


    public static void main(String[] args) throws Exception {
        CommandLineParser parser = new DefaultParser();
        Options options = createOptions();
        if (args.length == 0) {
            printHelp();
        } else {
            CommandLine line = parser.parse(options, args);
            if (line.hasOption("h")) {
                printHelp();
                return;
            }
            process(line);
        }
    }

    @SuppressWarnings("java:S2095")
    private void generateCss() throws IOException {
        new File(sourceDir + "/css").mkdirs();
        InputStream cssStream = getClass().getClassLoader().getResourceAsStream("file.css");
        IOUtils.copy(cssStream, new FileOutputStream(sourceDir + "/css/file.css"));
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
        generateCss();
        HtmlWriter htmlWriter = new HtmlWriter(new FileWriter(sourceDir + "/" + INDEX_HTML));
        routeFolder("", new File(sourceDir), htmlWriter);
        htmlWriter.close();
        TimeInstrument timeInstrument = profiler.stop();
        timeInstrument.log();

        avgColorImageStopWatcher.stop();
        readImageStopWatcher.stop();
        resizeImageStopWatcher.stop();
        logger.info("readImage     : {} ms", readImageStopWatcher.getTime(TimeUnit.MILLISECONDS));
        logger.info("avgColorImage : {} ms", avgColorImageStopWatcher.getTime(TimeUnit.MILLISECONDS));
        logger.info("resizeImage   : {} ms", resizeImageStopWatcher.getTime(TimeUnit.MILLISECONDS));
    }
}
