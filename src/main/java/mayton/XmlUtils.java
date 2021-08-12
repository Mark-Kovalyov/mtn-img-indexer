package mayton;

import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;

public class XmlUtils {

    private XmlUtils() {
        // No instance
    }

    public static class Singleton {
        public static final XmlUtils xmlUtils = new XmlUtils();
    }

    public static XmlUtils getInstance() {
        return Singleton.xmlUtils;
    }

    private TransformerFactory transformerFactory = TransformerFactory.newInstance();

    public Transformer prepareTransformer(Reader xsltReader) throws TransformerConfigurationException {
        StreamSource xslSource = new StreamSource(xsltReader);
        Templates xsltObject = transformerFactory.newTemplates(xslSource);
        Transformer transformer = xsltObject.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        return transformer;
    }

    public void transformToHtml(Reader xhtmlReader, Transformer transformer, Writer writer) throws TransformerException, IOException {
        transformer.transform(new StreamSource(xhtmlReader), new StreamResult(writer));
        writer.flush();
    }

    public void transformToHtml(Reader xhtmlReader, Reader xsltReader, Writer writer) throws TransformerException, IOException {
        Transformer transformer = prepareTransformer(xsltReader);
        transformer.transform(new StreamSource(xhtmlReader), new StreamResult(writer));
        writer.flush();
    }

    public void formatWithIndent(Reader xhtmlReader, Writer xhtmlWriter) throws TransformerException, IOException {
        Reader xsltReader = new InputStreamReader((getClass().getClassLoader().getResourceAsStream("xslt/empty-transform.xslt")));
        StreamSource xslSource = new StreamSource(xsltReader);
        Templates xsltObject = transformerFactory.newTemplates(xslSource);
        Transformer transformer = xsltObject.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.transform(new StreamSource(xhtmlReader), new StreamResult(xhtmlWriter));
        xhtmlWriter.flush();
    }



}
