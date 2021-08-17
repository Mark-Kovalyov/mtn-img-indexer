package mayton.phototimesort;

import javax.annotation.concurrent.Immutable;
import java.time.LocalDateTime;
import java.util.Optional;

@Immutable
public final class ExifDates {

    public final LocalDateTime dateTime;
    public final LocalDateTime DateTimeOriginal;
    public final LocalDateTime DateTimeDigitized;

    public ExifDates(LocalDateTime dateTime, LocalDateTime dateTimeOriginal, LocalDateTime dateTimeDigitized) {
        this.dateTime = dateTime;
        DateTimeOriginal = dateTimeOriginal;
        DateTimeDigitized = dateTimeDigitized;
    }
}
