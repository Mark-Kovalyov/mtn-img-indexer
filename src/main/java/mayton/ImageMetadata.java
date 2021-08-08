package mayton;

import javax.annotation.concurrent.Immutable;

@Immutable
public final class ImageMetadata {

    public final String src;
    public final int width;
    public final int height;
    public final String bgcolor;
    public final String alt;

    public ImageMetadata(String src, int width, int height, String bgcolor, String alt) {
        this.src = src;
        this.width = width;
        this.height = height;
        this.bgcolor = bgcolor;
        this.alt = alt;
    }
}
