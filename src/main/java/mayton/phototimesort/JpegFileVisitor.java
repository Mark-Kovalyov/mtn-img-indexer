package mayton.phototimesort;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JpegFileVisitor extends SimpleFileVisitor<Path> {

    public static Logger logger = LoggerFactory.getLogger(JpegFileVisitor.class);

    public static final Pattern JPEG_EXTENSION = Pattern.compile(".+\\.(?<extension>jpg|jfif|jpe|jpeg)$", Pattern.CASE_INSENSITIVE);

    private JpegDateTimeExtractor jpegDateTimeExtractor = JpegDateTimeExtractor.instance();

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        String path = file.toAbsolutePath().toString();
        logger.info("Vizit file {}", path);
        Matcher matcher = JPEG_EXTENSION.matcher(path);
        if (matcher.matches()) {
            logger.info("Processing JPEG file {}", path);
            Optional<LocalDateTime> dateTime = jpegDateTimeExtractor.fromFile(new FileInputStream(path));
            if (dateTime.isPresent()) {
                logger.info("Detected exif tag in {} is {}", path, dateTime);
            }
        }
        return FileVisitResult.CONTINUE;
    }

}
