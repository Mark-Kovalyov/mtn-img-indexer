package mayton.html;

import org.apache.commons.io.output.XmlStreamWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import java.io.Writer;

import static java.lang.String.valueOf;

public class XHtmlWriter implements HtmlWriter {

    private XMLStreamWriter xmlStreamWriter;

    private static Logger logger = LoggerFactory.getLogger(XmlStreamWriter.class);

    public XHtmlWriter(Writer writer, String title, String css) throws XMLStreamException {
        XMLOutputFactory factory = XMLOutputFactory.newInstance();
        // TODO: Implement indentation
        xmlStreamWriter = factory.createXMLStreamWriter(writer);
        xmlStreamWriter.writeStartDocument("utf-8","1.0");
        xmlStreamWriter.writeStartElement("html");
            xmlStreamWriter.writeStartElement("head");
                xmlStreamWriter.writeStartElement("title");
                    xmlStreamWriter.writeCharacters(title);
                xmlStreamWriter.writeEndElement();
                xmlStreamWriter.writeStartElement("meta");
                    xmlStreamWriter.writeAttribute("name", "viewport");
                    xmlStreamWriter.writeAttribute("content", "width=device-width, initial-scale=1.0");
                xmlStreamWriter.writeEndElement();
                xmlStreamWriter.writeStartElement("style");
                xmlStreamWriter.writeCharacters(css);
                xmlStreamWriter.writeEndElement();
            xmlStreamWriter.writeEndElement();
            xmlStreamWriter.writeStartElement("body");
    }

    @Override
    public void writeH1(String h1) {
        try {
            xmlStreamWriter.writeStartElement("h1");
            xmlStreamWriter.writeCharacters(h1);
            xmlStreamWriter.writeEndElement();
        } catch (XMLStreamException e) {
            logger.error("!", e);
        }
    }

    @Override
    public void writeH3(String h3) {
        try {
            xmlStreamWriter.writeStartElement("h3");
            xmlStreamWriter.writeCharacters(h3);
            xmlStreamWriter.writeEndElement();
        } catch (XMLStreamException e) {
            logger.error("!", e);
        }
    }

    @Override
    public void writeParagraph() {
        try {
            xmlStreamWriter.writeEmptyElement("p");
        } catch (XMLStreamException e) {
            logger.error("!", e);
        }
    }

    @Override
    public void lineBreak() {
        try {
            xmlStreamWriter.writeEmptyElement("br");
        } catch (XMLStreamException e) {
            logger.error("!", e);
        }
    }

    @Override
    public void beginTable(int tableWidthPixels) {
        try {
            xmlStreamWriter.writeStartElement("table");
            xmlStreamWriter.writeAttribute("width", valueOf(tableWidthPixels));
            xmlStreamWriter.writeAttribute("cellspacing", "0");
            xmlStreamWriter.writeAttribute("cellpadding", "0");
        } catch (XMLStreamException e) {
            logger.error("!", e);
        }
    }

    @Override
    public void beginRow() {
        try {
            xmlStreamWriter.writeStartElement("tr");
        } catch (XMLStreamException e) {
            logger.error("!", e);
        }
    }

    @Override
    public void endRow() {
        try {
            xmlStreamWriter.writeEndElement();
        } catch (XMLStreamException e) {
            logger.error("!", e);
        }
    }

    @Override
    public void beginDiv() {
        try {
            xmlStreamWriter.writeStartElement("div");
        } catch (XMLStreamException e) {
            logger.error("!", e);
        }
    }

    @Override
    public void endDiv() {
        try {
            xmlStreamWriter.writeEndElement();
        } catch (XMLStreamException e) {
            logger.error("!", e);
        }
    }

    @Override
    public void startTd() {
        try {
            xmlStreamWriter.writeStartElement("td");
        } catch (XMLStreamException e) {
            logger.error("!", e);
        }
    }

    @Override
    public void endTd() {
        try {
            xmlStreamWriter.writeEndElement();
        } catch (XMLStreamException e) {
            logger.error("!", e);
        }
    }

    @Override
    public void endTable() {
        try {
            xmlStreamWriter.writeEndElement();
        } catch (XMLStreamException e) {
            logger.error("!", e);
        }
    }

    @Override
    public void writeImg(String src, int width, int height) {
        try {
            xmlStreamWriter.writeStartElement("img");
            xmlStreamWriter.writeAttribute("src", src);
            xmlStreamWriter.writeAttribute("alt", "");
            xmlStreamWriter.writeAttribute("width", valueOf(width));
            xmlStreamWriter.writeAttribute("height", valueOf(height));
            xmlStreamWriter.writeEndElement();
        } catch (XMLStreamException e) {
            logger.error("!", e);
        }
    }

    @Override
    public void writeImg(String src) {
        try {
            xmlStreamWriter.writeStartElement("img");
            xmlStreamWriter.writeAttribute("src", src);
            xmlStreamWriter.writeAttribute("alt", "");
            xmlStreamWriter.writeEndElement();
        } catch (XMLStreamException e) {
            logger.error("!", e);
        }
    }

    @Override
    public void beginAnchor(String ref) {
        try {
            xmlStreamWriter.writeStartElement("a");
            xmlStreamWriter.writeAttribute("href", ref);
        } catch (XMLStreamException e) {
            logger.error("!", e);
        }
    }

    @Override
    public void emptyAnchor(String ref, String comment) {
        try {
            xmlStreamWriter.writeStartElement("a");
            xmlStreamWriter.writeAttribute("href", ref);
            xmlStreamWriter.writeCharacters(comment);
            xmlStreamWriter.writeEndElement();
        } catch (XMLStreamException e) {
            logger.error("!", e);
        }
    }

    @Override
    public void endAnchor() {
        try {
            xmlStreamWriter.writeEndElement();
        } catch (XMLStreamException e) {
            logger.error("!", e);
        }
    }

    @Override
    public void close() {
        try {
            xmlStreamWriter.writeEndDocument();
            xmlStreamWriter.close();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }
}
