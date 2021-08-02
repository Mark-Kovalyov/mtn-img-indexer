package mayton.html;

import java.io.PrintWriter;
import java.io.Writer;

import static org.apache.commons.text.StringEscapeUtils.escapeHtml4;

public class HtmlWriter implements AutoCloseable {

    PrintWriter printWriter;

    public HtmlWriter(Writer writer) {
        printWriter = new PrintWriter(writer);
        printWriter.println("<!DOCTYPE html>");
        printWriter.println("<html>");
        printWriter.println("<head>");
        printWriter.println("<meta charset=\"UTF-8\"/>");
        printWriter.println("<link rel=\"stylesheet\" href=\"css/file.css\"/>");
        printWriter.println("</head>");
        printWriter.println("<body>");
    }

    public void writeH1(String h1) {
        printWriter.print("<h1>");
        printWriter.print(escapeHtml4(h1));
        printWriter.print("</h1>\n");
    }

    public void writeH3(String h3) {
        printWriter.print("<h3>");
        printWriter.print(escapeHtml4(h3));
        printWriter.print("</h3>\n");
    }

    // <img src="html5.gif" alt="HTML5 Icon" width="128" height="128">
    // <img src="html5.gif" alt="HTML5 Icon" style="width:128px;height:128px;">
    // <img src="image.png" style="background-color:red;" />
/*    public void writeImg(String src, int width, int height, String bgColorHex) {
        printWriter.printf("<img src=\"%s\" ", escapeHtml4(src));
        printWriter.printf(" width=\"%d\" height=\"%s\" ", width, height);
        printWriter.printf(" style=\"background-color:%s;\" ", bgColorHex);
    }*/

    public void writeImg(String src, String style, int width, int height) {
        printWriter.printf("<img src=\"%s\" ", escapeHtml4(src));
        printWriter.printf(" style=\"%s\" loading=\"lazy\" ", style);
        printWriter.printf(" width=%d height=%d >%n", width, height);
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
