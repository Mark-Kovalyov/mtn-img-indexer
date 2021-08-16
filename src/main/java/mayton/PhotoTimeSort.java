package mayton;

import org.apache.commons.cli.Options;

public class PhotoTimeSort {

    public static Options createOptions() {
        return new Options()
                .addRequiredOption("s", "source", true, "Source jpeg files folder")
                .addRequiredOption("d", "source", true, "Dest folder")
                .addRequiredOption("u", "unsorded", true, "Unsorted dest");

    }

    public static void main(String[] args) {

    }

}
