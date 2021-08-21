package mayton;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("quicktests")
class DateTimeParserTest {

    @Test
    void testPattern1() {
        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy:MM:dd  H:mm:ss");

        String input1 = "2012:10:18  1:54:45";
        LocalDateTime localDateTime1 = LocalDateTime.parse(input1, pattern);
        assertEquals(LocalDateTime.of(2012,10,18,1,54,45), localDateTime1);

        DateTimeFormatter pattern2 = DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss");

        String input2 = "2012:10:18 11:54:45";
        LocalDateTime localDateTime2 = LocalDateTime.parse(input2, pattern2);
        assertEquals(LocalDateTime.of(2012,10,18,11,54,45), localDateTime2);
    }

}
