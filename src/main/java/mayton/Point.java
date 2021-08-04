package mayton;

import javax.annotation.concurrent.Immutable;

@Immutable
public final class Point {

    public final double x;
    public final double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }
}
