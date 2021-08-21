package mayton.phototimesort;

import org.apache.commons.cli.*;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import static mayton.FileUtils.replaceDot;

public class PhotoTimeSort {

    public static Logger logger = LoggerFactory.getLogger(PhotoTimeSort.class);

    private final CommandLine line;

    private String pathFormat = "yyyy/MM/dd/HH-mm-ss";

    private String exiftags = "";

    private List<String> timeFormats;

    public static Options createOptions() {
        return new Options()
                .addRequiredOption("s", "source", true, "Source jpeg files folder")
                .addRequiredOption("d", "dest", true, "Dest folder")
                .addOption("f", "timeformat", true, "Comma-separated local date-time format for exif tag. Default = 'yyyy:MM:dd HH:mm:ss'")
                .addOption("o", "pathformat", true, "Output folder format. Default = 'yyyy/MM/dd/HH-mm-ss'")
                .addOption("x", "exiftags", true, "Comma-separated exif-tags list. Default = 'DateTime,DateTimeOriginal,DateTimeDigitized'")
                .addOption("t", "trash", true, "Unrecognized files");
    }

    public static void printHelp() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("java -jar photo-time-sort.jar", createOptions(), true);
    }

    public PhotoTimeSort(CommandLine line) {
        this.line = line;
    }

    public void process() throws IOException {
        File sourceDir = new File(replaceDot(line.getOptionValue("s")));
        File trash = null;
        File destDir = new File(replaceDot(line.getOptionValue("d")));
        if (line.hasOption("f")) {
            timeFormats = Arrays.asList(line.getOptionValue("f").split(Pattern.quote(",")));
        }
        if (line.hasOption("o")) {
            pathFormat = line.getOptionValue("o");
        }
        if (line.hasOption("x")) {

        }
        if (line.hasOption("t")) {
            trash = new File(replaceDot(line.getOptionValue("t")));
        }
        routePhotos(sourceDir, destDir, trash);
    }

    private void routePhotos(File sourceDir, File destDir, File trash) throws IOException {
        if (sourceDir.equals(destDir) || sourceDir.equals(trash) || destDir.equals(trash)) {
            logger.error("This is strongly not recommended to peek the same folder for source and dest or trush in combinations");
            return;
        }
        new File(trash.getAbsolutePath()).mkdirs();
        JpegFileVisitor metaVisitor = new JpegFileVisitor(sourceDir, destDir, trash,  DateTimeFormatter.ofPattern("yyyy/MM/dd/HH-mm-ss"));
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
