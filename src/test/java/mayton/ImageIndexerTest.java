package mayton;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.regex.Matcher;

import static mayton.FileUtils.extractLastPathElement;
import static mayton.FileUtils.trimExtension;
import static mayton.ImageIndexer.JPEG_MINI_EXTENSION;
import static org.junit.jupiter.api.Assertions.*;

@Tag("quicktests")
class ImageIndexerTest {

    @Test
    void testTrimExtension() {
        assertEquals("hello", trimExtension("hello"));
        assertEquals("hello.world", trimExtension("hello.world.jpg"));
        assertEquals("1", trimExtension("1.jpg"));
    }

    @Test
    void testExtractPathElem() {
        assertEquals("image-001.jpg", extractLastPathElement("image-001.jpg"));
        assertEquals("image-001.jpg", extractLastPathElement("/image-001.jpg"));
        assertEquals("image-001.jpg", extractLastPathElement("/img/catalog01/image-001.jpg"));


    }

    @Test
    void testJpegMiniPattern() {
        Matcher matcher1 = JPEG_MINI_EXTENSION.matcher("18739064-mini.jpeg");
        assertTrue(matcher1.matches());
        assertNotNull(matcher1.group("suffix"));
        assertEquals("-mini", matcher1.group("suffix"));
        assertNotNull(matcher1.group("extension"));
        assertEquals("jpeg", matcher1.group("extension"));
    }

    @Test
    void testCropPath() {
        assertEquals("/bigdata/tmp/2011/07/01",FileUtils.cropLastPathElement("/bigdata/tmp/2011/07/01/01-02-55.jpg"));
    }
}
