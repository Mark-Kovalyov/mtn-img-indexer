package mayton;

import org.junit.jupiter.api.Test;
import javax.xml.transform.TransformerException;
import java.io.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class XmlUtilsTest {

    @Test
    public void testIndentation() throws TransformerException, IOException {

        String xml = "<?xml version=\"1.0\"?><html><head></head><body>Hello world</body></html>";
        StringWriter sw = new StringWriter();
        XmlUtils.getInstance().formatWithIndent(new StringReader(xml), sw);
        assertEquals(
                "<html>\n" +
                "    <head/>\n" +
                "    <body>Hello world</body>\n" +
                "</html>\n", sw.toString());
    }

    @Test
    public void testTransform() throws IOException, TransformerException {
        String xml = "<?xml version=\"1.0\"?><html><head></head></html>";
        Reader xslt = new InputStreamReader((getClass().getClassLoader().getResourceAsStream("xslt/transform-01.xslt")));
        StringWriter sw = new StringWriter();
        XmlUtils.getInstance().transformToHtml(new StringReader(xml), xslt, sw);
        assertEquals(
                "<html>\n" +
                "    <body>\n" +
                "        <h1>Title</h1>\n" +
                "    </body>\n" +
                "</html>\n", sw.toString());
    }

}
