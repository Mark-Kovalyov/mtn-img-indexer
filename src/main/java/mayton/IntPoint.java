package mayton;

import javax.annotation.concurrent.Immutable;

@Immutable
public final class IntPoint {

    public final int x;
    public final int y;

    public IntPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
