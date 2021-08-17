package mayton.phototimesort;

import org.apache.commons.cli.*;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;


public class PhotoTimeSort {

    public static Logger logger = LoggerFactory.getLogger(PhotoTimeSort.class);

    private final CommandLine line;

    public static Options createOptions() {
        return new Options()
                .addRequiredOption("s", "source", true, "Source jpeg files folder")
                .addRequiredOption("d", "dest", true, "Dest folder")
                .addOption("u", "unsorded", true, "Unsorted dest");

    }

    public static void printHelp() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("java -jar photo-time-sort.jar", createOptions(), true);
    }

    public PhotoTimeSort(CommandLine line) {
        this.line = line;
    }

    public void process() throws IOException {
        File sourceDir = new File(line.getOptionValue("s"));
        File destDir = new File(line.getOptionValue("d"));
        File unsorted = null;
        if (line.hasOption("u")) {
            unsorted = new File(line.getOptionValue("u"));
        }
        routePhotos(sourceDir, destDir, unsorted);
    }

    private void routePhotos(File sourceDir, File destDir, File unsorted) throws IOException {
        JpegFileVisitor metaVisitor = new JpegFileVisitor();
        StopWatch stopWatch = StopWatch.createStarted();
        Files.walkFileTree(Path.of(sourceDir.getAbsolutePath()), metaVisitor);
        stopWatch.stop();
        logger.info("Elapsed time : {} s", stopWatch.getTime(TimeUnit.SECONDS));
    }

    public static void main(String[] args) throws ParseException, IOException {
        CommandLineParser parser = new DefaultParser();
        Options options = createOptions();
        if (args.length == 0) {
            printHelp();
        } else {
            CommandLine line = parser.parse(options, args);
            PhotoTimeSort photoTimeSort = new PhotoTimeSort(line);
            photoTimeSort.process();
        }
    }

}
