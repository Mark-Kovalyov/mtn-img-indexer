package mayton.html;

public interface HtmlWriter extends AutoCloseable {

    void writeH1(String h1);

    void writeParagraph();

    void lineBreak();

    void beginTable(int tableWidthPixels);

    void beginRow();

    void endRow();

    void beginDiv();

    void endDiv();

    void td();

    void endTable();

    void writeImg(String id, String src, String style, int width, int height, String alt);

    void writeAnchor(String ref, String comment);

}
