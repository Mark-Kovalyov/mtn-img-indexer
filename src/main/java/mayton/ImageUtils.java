package mayton;

import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Triple;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ImageUtils {

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

}
