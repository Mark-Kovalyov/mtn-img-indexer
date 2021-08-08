package mayton.layouts;

import mayton.ImageMetadata;
import mayton.html.HtmlWriter;

import java.util.UUID;

import static java.lang.String.format;

public class RibbonStrategy extends ImageLayoutStrategy {

    private int currentPosition;

    private boolean rowIsOpened = false;

    public RibbonStrategy(HtmlWriter htmlWriter, int docWidthAdvice, int rowHeightAdvice) {
        super(htmlWriter, docWidthAdvice, rowHeightAdvice);
        currentPosition = 0;
    }

    @Override
    public void onStartDocument() {
        currentPosition = 0;
        htmlWriter.beginTable(docWidthAdvice);
        htmlWriter.beginRow();
        rowIsOpened = true;
    }

    @Override
    public void onAddImage(ImageMetadata imageMetadata) {
        double scaleFactor = (double) rowHeightAdvice / imageMetadata.height;
        int scaledWidth = (int) (scaleFactor * imageMetadata.width);
        if (currentPosition + scaledWidth > docWidthAdvice) {
            currentPosition = 0;
            htmlWriter.endRow();
            htmlWriter.endTable();
            htmlWriter.beginTable(docWidthAdvice);
            htmlWriter.beginRow();
        } else {
            currentPosition += scaledWidth;
        }
        htmlWriter.td();
        htmlWriter.writeImg(
                null, // UUID.randomUUID().toString(), 
                imageMetadata.src,
                null, //format("background-color:%s;", imageMetadata.bgcolor),
                scaledWidth,
                rowHeightAdvice,
                imageMetadata.alt);
    }

    @Override
    public void onEndDocument() {
        if (rowIsOpened) {
            htmlWriter.endRow();
            htmlWriter.endTable();
        }
    }

    @Override
    public void close() throws Exception {
        htmlWriter.close();
    }
}
