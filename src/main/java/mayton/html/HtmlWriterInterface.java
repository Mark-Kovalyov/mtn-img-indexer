package mayton.html;

import static org.apache.commons.text.StringEscapeUtils.escapeHtml4;

public interface HtmlWriterInterface extends AutoCloseable {

    void writeH1(String h1);

    void writeImg(String src, String style, int width, int height);

    void writeAnchor(String ref, String comment);

}
