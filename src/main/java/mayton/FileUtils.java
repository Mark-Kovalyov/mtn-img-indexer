package mayton;

import javax.xml.stream.XMLStreamException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

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

    public String transform(String xml) throws TransformerException {
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        Writer out = new StringWriter();
        transformer.transform(new StreamSource(new StringReader(xml)), new StreamResult(out));
        return out.toString();
    }

    public void transform(Reader reader, Writer writer) throws TransformerException {
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        transformer.transform(new StreamSource(reader), new StreamResult(writer));
    }

}
