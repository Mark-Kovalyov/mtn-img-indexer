package mayton.gradient;

import mayton.Point;

import javax.annotation.Nonnull;

public interface GradientOpmimizer {

    void init();

    @Nonnull Point[] optimize(@Nonnull double[] values, int maxPoints, double maxStandardDeviation);

}
