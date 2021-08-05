package mayton.html;

import java.io.PrintWriter;
import java.io.Writer;

// TODO: Re-write with in-box html or XML writer!
import static org.apache.commons.text.StringEscapeUtils.escapeHtml4;

public class HtmlWriter implements HtmlWriterInterface {

    PrintWriter printWriter;

    public HtmlWriter(Writer writer) {
        printWriter = new PrintWriter(writer);
        printWriter.println("<!DOCTYPE html>");
        printWriter.println("<html>");
        printWriter.println("<head>");
        printWriter.println("<meta charset=\"UTF-8\"/>");
        printWriter.println("<style>");
        printWriter.print("body {\n" +
                "  background-color: #5E2B89;\n" +
                "}\n" +
                "\n" +
                "table, th, td {\n" +
                "  border: 0px;\n" +
                "}\n" +
                "\n" +
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

    public void writeImg(String src, String style, int width, int height) {
        printWriter.printf("<img src=\"%s\" ", escapeHtml4(src));
        printWriter.printf(" style=\"%s\" loading=\"lazy\" ", style);
        printWriter.printf(" width=%d height=%d >%n", width, height);
    }

    public void writeParagraph() {
        printWriter.println("<p>");
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
