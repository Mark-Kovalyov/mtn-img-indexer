package mayton;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FrameTest {

    Frame fullHd = new Frame(0, 0, 1920, 1080);

    @Test
    void test1() {
        Frame panoramFrame = fullHd.fit(new Frame(0, 0, 5312, 1874));
        assertEquals(1920, panoramFrame.width());
        assertEquals(676, panoramFrame.height());
    }

    @Test
    void test2() {
        Frame narrowFrame = fullHd.fit(new Frame(0, 0, 800, 534));
        assertEquals(1616, narrowFrame.width());
        assertEquals(1080, narrowFrame.height());
    }


}