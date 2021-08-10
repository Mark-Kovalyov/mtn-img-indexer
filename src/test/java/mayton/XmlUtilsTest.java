package mayton;

import org.junit.jupiter.api.Test;

import javax.xml.transform.TransformerException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class XmlUtilsTest {

    @Test
    public void test() throws TransformerException {
        String xml = "<?xml version=\"1.0\"?><html><head></head></html>";
        assertEquals("", XmlUtils.transform(xml));
    }

}
