package mayton.html;

import java.io.PrintWriter;
import java.io.Writer;

// TODO: Re-write with
import static org.apache.commons.text.StringEscapeUtils.escapeHtml4;

public class HtmlWriterSimple implements HtmlWriter {

    PrintWriter printWriter;

    public HtmlWriterSimple(Writer writer) {
        printWriter = new PrintWriter(writer);
        printWriter.println("<!DOCTYPE html>");
        printWriter.println("<html>");
        printWriter.println("<head>");
        printWriter.println("<meta charset=\"UTF-8\"/>");
        printWriter.println("<style>");
        printWriter.print(
                "body {\n" +
                "  background-color: #5E2B89;\n" +
                "}\n" +
                "\n" +
                "table, th, td {\n" +
                "  border: none;\n" +
                "}\n" +
                "\n" +
                "img {\n" +
                "  display: block;\n" +
                "}\n" +
                "h1 {\n" +
                "  color: #EEE3CE;\n" +
                "  text-align: left;\n" +
                "  font-family: verdana;\n" +
                "  font-size: 20px;\n" +
                "}\n" +
                "\n" +
                "ul {\n" +
                "  color: #EEE3CE;\n" +
                "  font-family: verdana;\n" +
                "  font-size: 15px;\n" +
                "}\n" +
                "\n" +
                "li {\n" +
                "  color: #EEE3CE;\n" +
                "  font-family: verdana;\n" +
                "  font-size: 20px;\n" +
                "}\n" +
                "\n" +
                "a {\n" +
                "  color: #EEE3CE;\n" +
                "  font-family: verdana;\n" +
                "  font-size: 20px;\n" +
                "}\n");
        printWriter.println("</style>");
        printWriter.println("</head>");
        printWriter.println("<body>");
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
    public void td() {
        printWriter.printf("<td>");
    }

    @Override
    public void endTable() {
        printWriter.printf("</table>%n");
    }

    public void writeImg(String id, String src, String style, int width, int height, String alt) {
        printWriter.printf("   <img src=\"%s\" ", escapeHtml4(src));
        if (style != null) {
            printWriter.printf(" style=\"%s\" ", style);
        }
        printWriter.printf(" width=%d height=%d >%n", width, height);
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