package mayton;

public class FileUtils {

    // TODO: Check for all possible
    public static String SEPARATOR = System.getProperty("file.separator");

    public static String trimExtension(String absoluteFile) {
        int index = absoluteFile.lastIndexOf('.');
        return index > 0 ? absoluteFile.substring(0, index) : absoluteFile;
    }

    public static String extractLastPathElement(String path) {
        int index = path.lastIndexOf(SEPARATOR);
        return index >= 0 ? path.substring(index + 1) : path;
    }

}
