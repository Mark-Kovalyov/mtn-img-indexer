package mayton;


import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.concurrent.Immutable;

@Immutable
public final class Frame {

    static Logger logger = LoggerFactory.getLogger(Frame.class);

    public int x1;
    public int y1;
    public int x2;
    public int y2;

    public double ratio() {
        return (double) width() / height();
    }

    public int width() {
        return x2 - x1;
    }

    public int height() {
        return y2 - y1;
    }

    public IntPoint center() {
        return new IntPoint(x1 + width() / 2,y1 + height() / 2);
    }

    public int internalDiameter() {
        return Math.min(width(), height());
    }

    public Frame(int x1, int y1, int x2, int y2) {
        Validate.isTrue(x1 >= 0);
        Validate.isTrue(y1 >= 0, "y1 must be non-negative");
        Validate.isTrue(x2 >= 0);
        Validate.isTrue(y2 >= 0);
        Validate.isTrue(x1 <= x2, "The x1 must be less or equals to x2");
        Validate.isTrue(y1 <= y2, "The y1 must be less or equals to y2");
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    public Frame snapWithBorder(int borderPixels) {
        Validate.isTrue(borderPixels > 0);
        Validate.isTrue(borderPixels < width() / 2);
        Validate.isTrue(borderPixels < height() / 2);
        return new Frame(x1 + borderPixels, y1 + borderPixels, x2 - borderPixels, y2 - borderPixels);
    }

    public Frame fit(Frame that) {
        double k = that.ratio() / ratio();
        IntPoint center = center();
        int a = this.height();
        int b = this.width();
        int c = that.height();
        int d = that.width();
        if (k < 1.0) {
            int deltax = (a * d) / (c * 2);
            return new Frame(center.x - deltax, y1, center.x + deltax, y2);
        } else if (k > 1.0) {
            int deltay = (c * b) / (d * 2);
            return new Frame(x1, center.y - deltay, x2, center.y + deltay);
        } else {
            return this;
        }
    }

    public Pair<Frame, Frame> splitHorizontal(double percentage) {
        Validate.inclusiveBetween(0.0, 1.0, percentage);
        int h = (int) (percentage * height());
        return Pair.of(
                new Frame(x1, y1, x2, y1 + h),
                new Frame(x1, y1 + h, x2, y2));
    }

    public Pair<Frame, Frame> splitVertical(double percentage) {
        Validate.inclusiveBetween(0.0, 1.0, percentage);
        int v = (int) (percentage * width());
        return Pair.of(
                new Frame(x1, y1, x1 + v, y2),
                new Frame(x1 + v, y1, x2, y2));
    }

    @Override
    public String toString() {
        return "Frame{" +
                "x1=" + x1 +
                ", y1=" + y1 +
                ", x2=" + x2 +
                ", y2=" + y2 +
                ", width = " + width() +
                ", height = " + height() +
                ", ratio = " + String.format("%.4f", (double) width() / height()) + "}";
    }
}
