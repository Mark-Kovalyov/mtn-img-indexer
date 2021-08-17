package mayton.phototimesort;

import mayton.ImageIndexer;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.common.ImageMetadata;
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.apache.commons.imaging.formats.tiff.TiffField;
import org.apache.commons.imaging.formats.tiff.TiffImageMetadata;
import org.apache.commons.imaging.formats.tiff.fieldtypes.FieldType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

public class JpegDateTimeExtractor {

    public static Logger logger = LoggerFactory.getLogger(JpegDateTimeExtractor.class);

    private static class Singleton {
        public static final JpegDateTimeExtractor INSTANCE = new JpegDateTimeExtractor();
    }

    public static JpegDateTimeExtractor instance() {
        return Singleton.INSTANCE;
    }

    public Optional<String> safeGetValue(TiffField tiffField) {
        try {
            return Optional.of(tiffField.getStringValue());
        } catch (ImageReadException e) {
            return Optional.empty();
        }
    }

    public ExifDates exifDatesFromFile(InputStream inputStream) {
        return new ExifDates(Optional.empty());
    }

    public Optional<LocalDateTime> fromFile(InputStream inputStream) {
        ImageMetadata metadata = null;
        Optional<String> value = Optional.empty();
        try {
            metadata = Imaging.getMetadata(inputStream, null);
            if (metadata != null) {
                TiffImageMetadata items = ((JpegImageMetadata) metadata).getExif();
                if (items != null) {
                    for (TiffField field : items.getAllFields()) {
                        if (field.getFieldType() == FieldType.ASCII) {
                            String tagName = field.getTagName();
                            if ("DateTime".equals(tagName)) {
                                value = safeGetValue(field);
                                if (value.isPresent()) {
                                    break;
                                }
                            }
                            // TODO: DateTime DateTimeOriginal DateTimeDigitized
                        }
                    }
                } else {
                    logger.warn("No tiff fields");
                }
            } else {
                logger.warn("No TiffImageMetadata!");
            }
            if (value.isPresent()) {
                return Optional.of(LocalDateTime.parse(value.get(), DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss")));
            }
            //JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;
        } catch (ImageReadException e) {
            logger.error("!", e);
        } catch (IOException e) {
            logger.error("!", e);
        } catch (DateTimeParseException e) {
            logger.warn("DateTimeParseException during parse {}" + value.get(), e);
        }
        return Optional.empty();
    }

}
