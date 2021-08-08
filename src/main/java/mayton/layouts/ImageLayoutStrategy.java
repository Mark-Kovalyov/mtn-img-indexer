package mayton.layouts;

import mayton.ImageMetadata;
import mayton.html.HtmlWriter;

import javax.annotation.Nonnull;

// TODO: This is not a strategy... Maybe decorator?
public abstract class ImageLayoutStrategy implements AutoCloseable {

    protected HtmlWriter htmlWriter;
    protected int docWidthAdvice;
    protected int rowHeightAdvice;

    public ImageLayoutStrategy(@Nonnull HtmlWriter htmlWriter, int docWidthAdvice, int rowHeightAdvice) {
        this.htmlWriter = htmlWriter;
        this.docWidthAdvice = docWidthAdvice;
        this.rowHeightAdvice = rowHeightAdvice;
    }

    public abstract void onStartDocument();

    public abstract void onAddImage(@Nonnull ImageMetadata imageMetadata);

    public abstract void onEndDocument();

    @Deprecated() // TODO: Wrong access. Get rid of getter. Replace with correct design pattern.
    public HtmlWriter getHtmlWriter() {
        return htmlWriter;
    }
}
