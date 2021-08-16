package mayton;

import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Triple;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class ImageUtils {

    public static final double GK=0.587;
    public static final double BK=0.114;
    public static final double RK=0.299;



    public static final int FULL_HD_W = 1920;
    public static final int FULL_HD_H = 1080;

    public static final int PAL_W = 720;
    public static final int PAL_H = 576;

    public static final int VGA_W = 640;
    public static final int VGA_H = 480;

    public static int getRPixel(int color) {
        return (0x00FF0000&color)>>16;
    }

    public static int getBPixel(int color) {
        return (0x000000FF&color);
    }

    public static int getGPixel(int color) {
        return (0x0000FF00&color)>>8;
    }

    public static int getPixel(int R, int G, int B) {
        R = min(255, max(0, R));
        G = min(255, max(0, G));
        B = min(255, max(0, B));
        return 0xFF000000 | R << 16 | G << 8 | B;
    }

    public static String  tripleToHex(Triple<Double, Double, Double> color) {
        String res = String.format("#%02X%02X%02X",
                (int) (255.0 * color.getLeft()),
                (int) (255.0 * color.getMiddle()),
                (int) (255.0 * color.getRight()));
        return res;
    }

    public static Triple<Double, Double, Double> averageColor(BufferedImage image) {
        int x0 =0;
        int y0 =0;
        int w = image.getWidth();
        int h = image.getHeight();
        int x1 = x0 + w;
        int y1 = y0 + h;
        long sumr = 0, sumg = 0, sumb = 0;
        for (int x = x0; x < x1; x++) {
            for (int y = y0; y < y1; y++) {
                Color pixel = new Color(image.getRGB(x, y));
                sumr += pixel.getRed();
                sumg += pixel.getGreen();
                sumb += pixel.getBlue();
            }
        }
        int num = w * h;
        Triple<Double, Double, Double> res = ImmutableTriple.of(
                sumr / num / 255.0,
                sumg / num / 255.0,
                sumb / num / 255.0);
        return res;
    }

    public static BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) throws IOException {
        Image resultingImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_DEFAULT);
        BufferedImage outputImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        outputImage.getGraphics().drawImage(resultingImage, 0, 0, null);
        return outputImage;
    }

    public static void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static double getYPixelDouble(int color) {
        double res = (GK * ((color & 0x00FF00) >> 8) + BK * (color & 0x0000FF) + RK * ((color & 0xFF0000) >> 16)) / 255.0;
        assert res >= 0.0;
        assert res <= 1.0;
        return res;
    }

    public static final double UPIXEL_MAX_DOUBLE = getUPixelDouble(getPixel(0,255,0));
    public static final double UPIXEL_MIN_DOUBLE = getUPixelDouble(getPixel(255,0,255));

    public static double getUPixelDouble(int color) {
        return (
                -0.147 * (color & 0xFF)
                        - 0.289 * ((color & 0xFF0000) >> 16)
                        + 0.436 * ((color & 0xFF00) >> 8) + 111.18) / 222.36;
    }

    public static double getVPixelDouble(int color) {
        return  (0.615 * (color & 0xFF)
                        - 0.515 * ((color & 0xFF0000) >> 16)
                        - 0.1 * ((color & 0xFF00) >> 8) + 156.825) / 313.65;
    }

}
