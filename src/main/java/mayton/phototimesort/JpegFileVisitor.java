package mayton.phototimesort;

import mayton.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JpegFileVisitor extends SimpleFileVisitor<Path> {

    public static Logger logger = LoggerFactory.getLogger(JpegFileVisitor.class);

    public static final Pattern JPEG_EXTENSION = Pattern.compile(".+\\.(?<extension>jpg|jfif|jpe|jpeg)$", Pattern.CASE_INSENSITIVE);

    private JpegDateTimeExtractor jpegDateTimeExtractor = JpegDateTimeExtractor.instance();

    private File destDir;
    private File sourceDir;
    private File trash;

    private DateTimeFormatter dateTimeFormatter;

    public JpegFileVisitor(File sourceDir, File destDir, File trash, DateTimeFormatter dateTimeFormatter) {
        this.sourceDir = sourceDir;
        this.destDir = destDir;
        this.trash = trash;
        this.dateTimeFormatter = dateTimeFormatter;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        String path = file.toAbsolutePath().toString();
        logger.info("Vizit file path = {}", path);
        logger.debug("srcDir = {}", sourceDir.getAbsolutePath());
        logger.debug("destDir = {}", destDir.getAbsolutePath());
        Matcher matcher = JPEG_EXTENSION.matcher(path);
        if (matcher.matches()) {
            logger.info("Processing JPEG file {}", path);
            String extension = matcher.group("extension");
            Optional<LocalDateTime> dateTime = jpegDateTimeExtractor.fromFile(new FileInputStream(path));
            if (dateTime.isPresent()) {
                logger.info("Detected exif tag in {} is {}", path, dateTime);
                String syntheticPath = dateTimeFormatter.format(dateTime.get());
                logger.debug("syntheticPath = {}", syntheticPath);
                // TODO: This it not a good idea to create folder every time. Should be fixed.
                new File(destDir, FileUtils.cropLastPathElement(syntheticPath)).mkdirs();
                String jpegPath = destDir.getAbsolutePath() + FileUtils.SEPARATOR + syntheticPath + "." + extension;
                logger.debug("jpegPath = {}",  jpegPath);
                try(InputStream i = new FileInputStream(path);
                    OutputStream o = new FileOutputStream(jpegPath)) {
                    // TODO: Windows: implement zero-copy option with
                    //      - hardlinks if possible
                    // TODO: Linux: implement zero-copy option with
                    //      - COW (cp --reflink) for Btrfs, XFS
                    //      - symlinks for all filesystems
                    IOUtils.copy(i, o);
                }
            } else {
                logger.warn("Unable to detect exif-date attribute set in file {}", path);
                if (trash != null) {
                    logger.debug("trash path = {}", trash.getAbsolutePath());
                    try(InputStream i = new FileInputStream(path);
                        OutputStream o = new FileOutputStream(trash.getAbsolutePath() + FileUtils.SEPARATOR + UUID.randomUUID().toString() + "." + extension)) {
                        IOUtils.copy(i, o);
                    }
                }
            }
        }
        return FileVisitResult.CONTINUE;
    }

}
