package mayton;

import mayton.html.HtmlWriter;
import mayton.html.XHtmlWriter;
import org.apache.commons.cli.*;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.profiler.Profiler;
import org.slf4j.profiler.TimeInstrument;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.String.valueOf;
import static java.nio.charset.StandardCharsets.UTF_8;
import static mayton.FileUtils.extractLastPathElement;
import static mayton.FileUtils.trimExtension;
import static mayton.ImageUtils.*;
import static org.apache.commons.lang3.StringUtils.replace;

// mayton : Jul-30, 2021 - Initial commit
// mayton : 1-Aug, 2021 - changes
// mayton : 4-Aug, 2021 - changes
// mayton : 8-Aug, 2021

public class ImageIndexer {

    private StopWatch readImageStopWatcher = StopWatch.create();
    private StopWatch writeImageStopWatcher = StopWatch.create();
    private StopWatch resizeImageStopWatcher = StopWatch.create();

    public static final String MINI_SUFFIX = "-mini";
    public static final String INDEX_HTML = "index.html";
    public static final Pattern JPEG_MINI_EXTENSION = Pattern.compile(".+?(?<suffix>-(mini|bars|gradient))?\\.(?<extension>jpg|jfif|jpe|jpeg|png)$", Pattern.CASE_INSENSITIVE);

    public static Logger logger = LoggerFactory.getLogger(ImageIndexer.class);
    private final String sourceDir;
    private int targetHeightSize;
    private String rootFolderName;
    private String bgColor = "#000000";
    private String textColor = "#FFFFFF";
    private Transformer transformer = null;
    private String css;

    private CommandLine commandLine;

    public ImageIndexer(CommandLine line) throws IOException, TransformerConfigurationException {
        commandLine      = line;
        sourceDir        = commandLine.getOptionValue("s");
        targetHeightSize = commandLine.hasOption("h") ? Integer.parseInt(commandLine.getOptionValue("h")) : 128;
        rootFolderName   = commandLine.hasOption("r") ? commandLine.getOptionValue("r") : "";
        bgColor          = commandLine.hasOption("b") ? commandLine.getOptionValue("b") : bgColor;
        textColor        = commandLine.hasOption("t") ? commandLine.getOptionValue("t") : textColor;

        if (commandLine.hasOption("c")) {
            css = IOUtils.toString(new FileReader(commandLine.getOptionValue("c"), UTF_8));
        } else {
            css = IOUtils.toString(getClass().getClassLoader().getResourceAsStream("css/default.css"), UTF_8);
            // TODO: Huh... looks like FreeMaker or Thymeleaf ?
            css = replace(css, "${height}",    valueOf(targetHeightSize));
            css = replace(css, "${bgcolor}",   bgColor);
            css = replace(css, "${textcolor}", textColor);
        }
        if (commandLine.hasOption("t")) {
            transformer = XmlUtils.getInstance().prepareTransformer(new FileReader(commandLine.getOptionValue("t")));
        }
    }

    public static Options createOptions() {
        return new Options()
                .addRequiredOption("s", "source",  true, "Source jpeg files folder")
                .addOption("h", "thumnailheight",  true, "Thumbnail height size in pixels (default = 256)")
                .addOption("r", "rootfoldername",  true, "Root folder name. Example: 'My Photos'")
                .addOption("b", "bgcolor",         true, "Background color. Example #000000")
                .addOption("t", "textcolor",       true, "Text color. Example #0000FF")
                .addOption("c", "css",             true, "Peek external css file. Example 'my-file.css'");
                //.addOption("t", "transform",       true, "Transform xhtml-index-file with external XSLT script. Example 'my-styles.xslt'");
    }

    public static void printHelp() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("java -jar image-indexer.jar", createOptions(), true);
    }

    public void processFile(String localFolderPrefix, File file, HtmlWriter htmlWriter) {
        String fileName = extractLastPathElement(file.getName());
        Matcher miniMatcher = JPEG_MINI_EXTENSION.matcher(fileName);
        if (miniMatcher.matches() && miniMatcher.group("suffix") == null) {
            String extension = miniMatcher.group("extension");
            logger.info("process jpeg file {}", file);
            try {
                //readImageStopWatcher.resume();
                BufferedImage image = ImageIO.read(file);
                //readImageStopWatcher.suspend();
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
                    writeImageStopWatcher.resume();
                    try (OutputStream outputStream = new FileOutputStream(miniPath)) {
                        ImageIO.write(thumbnail, "JPEG", outputStream);
                        writeImageStopWatcher.suspend();
                    }
                    String src = trimExtension(fileName) + MINI_SUFFIX + "." + extension;
                    htmlWriter.beginAnchor(fileName);
                    htmlWriter.writeImg(src, thumbnailX, thumbnailY);
                    htmlWriter.endAnchor();
                }
            } catch (IIOException ex) {
                logger.error("ImageIO exception", ex);
            } catch (IOException ex) {
                logger.error("IO exception", ex);
            } catch (IllegalArgumentException ex) {
                logger.error("IllegalArgumentException", ex);
            } finally {
                /*if (readImageStopWatcher.isStarted()) {
                    readImageStopWatcher.suspend();
                }*/
            }
        }
    }

    public void routeFolder(String rootName, String htmlLocalFolder, File folder, HtmlWriter parentHtmlWriter) throws Exception {
        String title = rootName == null ? folder.getName() : rootName;
        parentHtmlWriter.writeH1(title);
        for(File node : folder.listFiles()) {
            if (node.isDirectory()) {
                String nodeName = node.getName();
                if (!nodeName.startsWith(".")) {
                    copyFolderImage(folder);
                    parentHtmlWriter.beginAnchor(node.getName() + "/" + INDEX_HTML);
                    parentHtmlWriter.writeImg("folder.svg", targetHeightSize, targetHeightSize);
                    parentHtmlWriter.writeH3(node.getName());
                    parentHtmlWriter.endAnchor();
                    parentHtmlWriter.lineBreak();

                    PipedReader pipedReader = new PipedReader(4096);
                    PipedWriter pipedWriter = new PipedWriter();
                    pipedWriter.connect(pipedReader);

                    HtmlWriter currentHtmlWriter = new XHtmlWriter(pipedWriter, title, css);

                    Thread thread = new Thread(() -> {
                        try {
                            logger.info("p1");
                            FileWriter writer = new FileWriter(node.getAbsoluteFile() + "/" + INDEX_HTML);
                            logger.info("p2");
                            //CountingReader countingReader = new CountingReader(pipedReader);
                            //XmlUtils.getInstance().transformToHtml(countingReader, this.transformer, writer);
                            IOUtils.copy(pipedReader, writer);
                            logger.info("p3");
                            writer.close();
                            logger.info("p4");
                        } catch (IOException e) {
                            logger.error("!", e);
                        }
                    });
                    thread.start();

                    routeFolder(null, htmlLocalFolder.isBlank() ? nodeName : htmlLocalFolder + "/" + nodeName,
                            node, currentHtmlWriter);
                    currentHtmlWriter.close();

                    logger.info("p5");
                    thread.join();
                    logger.info("p6");
                } else {
                    logger.debug("Ignore hidden folder {}", nodeName);
                }
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

    private void copyFolderImage(File folder)  {
        try(OutputStream os = new FileOutputStream(new File(folder, "folder.svg"))) {
            IOUtils.copy(getClass().getClassLoader().getResourceAsStream("img/folder.svg"), os);
        } catch (FileNotFoundException e) {
            logger.warn("!",e);
        } catch (IOException e) {
            logger.warn("!",e);
        }
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

        readImageStopWatcher.start();
        readImageStopWatcher.suspend();
        resizeImageStopWatcher.start();
        resizeImageStopWatcher.suspend();
        writeImageStopWatcher.start();
        writeImageStopWatcher.suspend();

        Profiler profiler = new Profiler("image-indexer");
        profiler.setLogger(logger);
        profiler.start("indexing");
        HtmlWriter htmlWriter = new XHtmlWriter(new FileWriter(sourceDir + "/" + INDEX_HTML), rootFolderName, css);
        routeFolder(rootFolderName, rootFolderName, new File(sourceDir), htmlWriter);
        htmlWriter.close();
        TimeInstrument timeInstrument = profiler.stop();
        timeInstrument.log();


        readImageStopWatcher.stop();
        resizeImageStopWatcher.stop();
        writeImageStopWatcher.stop();

        logger.info("Statistics: ");
        logger.info("readImage elapsed time          : {} ms", readImageStopWatcher.getTime(TimeUnit.MILLISECONDS));
        logger.info("resize image elapsed time       : {} ms", resizeImageStopWatcher.getTime(TimeUnit.MILLISECONDS));
        logger.info("write thumbnails elapsed time   : {} ms", writeImageStopWatcher.getTime(TimeUnit.MILLISECONDS));
    }
}
