package mayton.html;

import j2html.TagCreator;
import j2html.tags.attributes.ISrc;

import java.io.PrintWriter;

import static j2html.TagCreator.body;

public class Html5Writer implements HtmlWriterInterface {

    private PrintWriter printWriter;

    private TagCreator tagCreator;

    @Override
    public void writeH1(String h1) {

        /*body(
                h1("Hello, World!"),
                img().withSrc("/img/hello.png")
        ).render();*/
    }


    @Override
    public void writeImg(String src, String style, int width, int height) {

    }

    @Override
    public void writeAnchor(String ref, String comment) {

    }

    @Override
    public void close() throws Exception {

    }
}
