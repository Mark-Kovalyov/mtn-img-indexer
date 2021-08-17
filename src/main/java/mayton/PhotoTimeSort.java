package mayton;

import org.apache.commons.cli.*;

import java.io.File;


public class PhotoTimeSort {


    private final CommandLine line;

    public static Options createOptions() {
        return new Options()
                .addRequiredOption("s", "source", true, "Source jpeg files folder")
                .addRequiredOption("d", "source", true, "Dest folder")
                .addOption("u", "unsorded", true, "Unsorted dest");

    }

    public static void printHelp() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("java -jar photo-time-sort.jar", createOptions(), true);
    }

    public PhotoTimeSort(CommandLine line) {
        this.line = line;
    }



    public void process() {
        File sourceDir = new File(line.getOptionValue("s"));
        File destDir = new File(line.getOptionValue("s"));
        File unsorted = null;
        if (line.hasOption("u")) {
            unsorted = new File(line.getOptionValue("u"));
        }
        routePhotos(sourceDir, destDir, unsorted);
    }

    private void routePhotos(File sourceDir, File destDir, File unsorted) {

    }

    public static void main(String[] args) throws ParseException {
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
