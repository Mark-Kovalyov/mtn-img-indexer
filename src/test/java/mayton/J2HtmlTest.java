package mayton;

import j2html.attributes.Attr;
import j2html.tags.specialized.HtmlTag;
import org.junit.jupiter.api.Test;

import static j2html.TagCreator.a;
import static j2html.TagCreator.body;
import static j2html.TagCreator.head;
import static j2html.TagCreator.html;
import static j2html.TagCreator.img;
import static j2html.TagCreator.meta;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class J2HtmlTest {

    @Test
    void testJ2html() {
        HtmlTag res = html(
                head(
                        meta()),
                body(
                        img(),
                        img()
                )
        );
        assertEquals("<html><head><meta></head><body><img><img></body></html>", res.toString());
    }

}
