package mayton.html;

public interface HtmlWriter extends AutoCloseable {

    void writeH1(String h1);

    void writeH3(String h3);

    void writeParagraph();

    void lineBreak();

    void beginTable(int tableWidthPixels);

    void beginRow();

    void endRow();

    void beginDiv();

    void endDiv();

    void startTd();

    void endTd();

    void endTable();

    void writeImg(String id, String src, String style, int width, int height, String alt);

    void beginAnchor(String ref);

    void emptyAnchor(String ref, String comment);

    void endAnchor();

}
