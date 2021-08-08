package mayton.html;

import java.io.PrintWriter;
import java.io.Writer;

// TODO: Re-write with
import static org.apache.commons.text.StringEscapeUtils.escapeHtml4;

public class HtmlWriterSimple implements HtmlWriter {

    PrintWriter printWriter;

    public HtmlWriterSimple(Writer writer) {
        printWriter = new PrintWriter(writer);
        printWriter.println("<!DOCTYPE html>\n" +
                "\n" +
                "<html>\n" +
                "    <head>\n" +
                "        <title>[title is here]</title>\n" +
                "        <meta charset=\"UTF-8\">\n" +
                "\n" +
                "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "        <style>\n" +
                "          \n" +
                "            img{\n" +
                "                display: block;\n" +
                "                height:  64px;\n" +
                "                margin-right: 1px;\n" +
                "                margin-bottom: 1px; \n" +
                "            }\n" +
                "            div{\n" +
                "                display: flex;\n" +
                "                flex-wrap: wrap;\n" +
                "                width: 95%;\n" +
                "            }\n" +
                "        </style>\n" +
                "    </head>\n" +
                "    <body>");
    }

    public void writeH1(String h1) {
        printWriter.print("<h1>");
        printWriter.print(escapeHtml4(h1));
        printWriter.print("</h1>\n");
    }

    @Override
    public void beginTable(int tableWidthPixels) {
        printWriter.printf("<table width=%d cellspacing=\"0\" cellpadding=\"0\">%n", tableWidthPixels);
    }

    @Override
    public void beginRow() {
        printWriter.printf(" <tr>%n");
    }

    @Override
    public void endRow() {
        printWriter.printf("</tr>%n");
    }

    @Override
    public void beginDiv() {
        printWriter.printf("<div>");
    }

    @Override
    public void endDiv() {
        printWriter.printf("</div>");
    }

    @Override
    public void td() {
        printWriter.printf("<td>");
    }

    @Override
    public void endTable() {
        printWriter.printf("</table>%n");
    }

    public void writeImg(String id, String src, String style, int width, int height, String alt) {
       printWriter.printf("<img src=\"%s\" alt=\"\"/>%n", src);
    }

    public void writeParagraph() {
        printWriter.println("<p>");
    }

    @Override
    public void lineBreak() {
        printWriter.printf("<br>%n");
    }

    public void writeAnchor(String ref, String comment) {
        printWriter.printf("<a href=\"%s\">%s</a>%n",
                escapeHtml4(ref),
                escapeHtml4(comment));
    }


    @Override
    public void close() throws Exception {
        printWriter.println("</body>");
        printWriter.close();
    }
}
