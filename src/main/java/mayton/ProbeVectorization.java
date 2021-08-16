package mayton;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static mayton.ImageUtils.*;

public class ProbeVectorization {

    static Logger logger = LoggerFactory.getLogger(YUVDecomposer.class);


    public static BufferedImage renderRgbImageFromFloatQuaternions(int w, int h, float[] array, double ratio) {
        Validate.isTrue(array.length % 4 == 0);
        BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        int k = 0;
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                image.setRGB(x, y, ImageUtils.getPixel(
                        (int) (array[k + 0] * ratio),
                        (int) (array[k + 1] * ratio),
                        (int) (array[k + 2] * ratio)
                ));
                k += 4;
            }
        }
        return image;
    }

    public static BufferedImage renderGrayImageFromFloatArray(int w, int h, float[] array, double ratio) {
        BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        int i = 0;
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int v = (int) (array[i] * ratio);
                image.setRGB(x, y, v << 16 | v << 8 | v);
            }
        }
        return image;
    }

    public static float[] prepareRGBRatios(int w, int h) {
        int pixels = w * h;
        float[] yuvRatios = new float[pixels * 4];
        int i = 0;
        for (int j = 0; j < pixels; j++) {
            yuvRatios[i++] = (float) RK;
            yuvRatios[i++] = (float) GK;
            yuvRatios[i++] = (float) BK;
            yuvRatios[i++] = 0.0f; // Placeholder to be aligned
        }
        return yuvRatios;
    }

    public static float[] vectorMultiply(float[] vector1, float[] vector2) {
        Validate.isTrue(vector1.length % 4 == 0);
        Validate.isTrue(vector2.length % 4 == 0);

        int j = 0;
        int i;
        // #pragma force jvm vectorization :)
        //       See: SSE :: MULSS/MULPS/ADDSS/ADDPS
        float[] result = new float[vector1.length];
        for (i = 0; i < vector1.length; i += 4) {
            result[i + 0] = vector1[i + 0] * vector2[i + 0]; // Ri * RK
            result[i + 1] = vector1[i + 1] * vector2[i + 1]; // Gi * GK
            result[i + 2] = vector1[i + 2] * vector2[i + 2]; // Bi * Bk
            result[i + 3] = vector1[i + 3] * vector2[i + 3]; // Placeholder
        }
        return result;
    }

    public static float[] mapByFour(float[] vector) {
        Validate.isTrue(vector.length % 4 == 0);
        float[] res = new float[vector.length / 4];
        int k = 0;
        for (int i = 0; i < vector.length; i += 4) {
            float sum = 0.0f;
            sum += vector[i + 0];
            sum += vector[i + 1];
            sum += vector[i + 2];
            sum += vector[i + 3];
            res[k] = sum;
            k++;
        }
        return res;
    }

    public static double scalarMultiply(float[] vector1, float[] vector2) {
        // TODO:
        return 0.0;
    }

    public static float[] bufferedImageToFloatArray(BufferedImage bufferedImage) {
        int W = bufferedImage.getWidth();
        int H = bufferedImage.getHeight();
        float[] floats = new float[W * H * 4];
        int i = 0;
        int color = 0;
        for (int y = 0; y < H; y++) {
            for (int x = 0; x < W; x++) {
                color = bufferedImage.getRGB(x, y);
                floats[i++] = ImageUtils.getRPixel(color);
                floats[i++] = ImageUtils.getGPixel(color);
                floats[i++] = ImageUtils.getBPixel(color);
                i++;
            }
        }
        return floats;
    }


    @SuppressWarnings({"java:S125", "java:S1135"})
    public static void main(String[] args) throws IOException {
        // Convert from RGB to floating points quaternions
        String sourcePath = args[0];

        StopWatch sourceReadStopWatcher = StopWatch.createStarted();
        BufferedImage source = ImageIO.read(new FileInputStream(sourcePath));
        sourceReadStopWatcher.stop();
        final int W = source.getWidth();
        final int H = source.getHeight();
        final int PIXELS = W * H;

        //MemoryImageSource memoryImageSource = new MemoryImageSource(W, H, new int[0], 0, W);
        //Canvas canvas = new Canvas();
        //Image image1 = canvas.createImage(memoryImageSource);

        logger.info("W = {}, H = {}, colorModel = {}", W, H, source.getColorModel());

        // 4 * sizeOf(float) = 4 * 32bit = 128bit. This is the same as single XMM register.

        float[] rgbSourceFloat = bufferedImageToFloatArray(source);
        logger.info("rgbSourceFloat size = {}", rgbSourceFloat.length);

        float[] yuvRatios = prepareRGBRatios(W, H);
        logger.info("ratios size = {}", rgbSourceFloat.length);

        // YUV-Components
        float[] yuvComponents = vectorMultiply(rgbSourceFloat, yuvRatios);
        // YUV-Plane
        float[] yPlane = mapByFour(yuvComponents);
        // yImage
        BufferedImage yImage = renderGrayImageFromFloatArray(W, H, yPlane, 1.0);

        // TODO: Render U, V


        BufferedImage test = renderGrayImageFromFloatArray(W, H, bufferedImageToFloatArray(source), 1.0);

        // TODO: Generate U, V images

        // Export YUV planes as 4 split-screen images to be clear
        BufferedImage dest = new BufferedImage(W * 2, H * 2, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = (Graphics2D) dest.getGraphics();
        //ImageObserver imageObserver = new YUVImageObserver(); // TODO: WTF? How to sync with fucken image observer?

        g2d.drawImage(source, 0, 0, null);
        sleep(5 * 1000L);
        g2d.drawImage(yImage, W, 0, null);
        sleep(5 * 1000L);
        g2d.drawImage(yImage, 0, H, null);
        sleep(5 * 1000L);
        g2d.drawImage(yImage, W, H, null);
        sleep(5 * 1000L);



        /*final Object done = new Object();
        ImageObserver imageObserver = (image, flags, x, y, width, height) -> {
            if (flags < ImageObserver.ALLBITS) {
                return true;
            } else {
                synchronized (done) {
                    done.notify();
                }
                return false;
            }
        };

        synchronized (done) {
            boolean completelyLoaded = g2d.drawImage(source, 0, 0, imageObserver);
            if (!completelyLoaded) {
                while (true) {
                    try {
                        done.wait(0);
                        break;
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }*/


        // TODO: Place Y, U, V images into 2,3,4x splits

        StopWatch destSaveStopWatch = StopWatch.createStarted();
        ImageIO.write(dest, "PNG", new FileOutputStream(FileUtils.trimExtension(sourcePath) + "-YUV-4x-split.png"));
        destSaveStopWatch.stop();

        logger.info("source read  : {} ms", sourceReadStopWatcher.getTime(TimeUnit.MILLISECONDS));
        //logger.info("renderYPlane : {} ms", renderYPlaneStopWatcher.getTime(TimeUnit.MILLISECONDS));
        //logger.info("renderUPlane : {} ms", renderUPlaneStopWatcher.getTime(TimeUnit.MILLISECONDS));
        logger.info("dest save    : {} ms", destSaveStopWatch.getTime(TimeUnit.MILLISECONDS));


    }
}