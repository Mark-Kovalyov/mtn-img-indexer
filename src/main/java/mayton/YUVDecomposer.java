package mayton;

import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Map;

import static java.awt.RenderingHints.*;
import static mayton.ImageUtils.*;

// 14-Aug, 2021 - mayton
// 16-Aug, 2021

public class YUVDecomposer {

    static Logger logger = LoggerFactory.getLogger(YUVDecomposer.class);

    static final int FPS = 60; // Video frames per/second

    // ffmpeg -r 1/5 -i img%03d.png -c:v libx264 -vf fps=25 -pix_fmt yuv420p out.mp4
    // ffmpeg -i input.mp4 -c:v libvpx-vp9 -b:v 2M output.webm
    //

    public static Pair<BufferedImage,float[]> horizontalFiltered(BufferedImage grayImage) {
        int H = grayImage.getHeight();
        int W = grayImage.getWidth();
        float[] floats = new float[H];
        BufferedImage horizontalFiltered = new BufferedImage(W, H, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < H; y++) {
            double ySum = 0.0;
            for (int x = 0; x < W; x++) {
                ySum += ImageUtils.getYPixelDouble(grayImage.getRGB(x, y));
            }
            floats[y] = (float) (ySum / W);
            int iavg = (int) (255.0 * ySum / W);
            for (int x = 0; x < W; x++) {
                horizontalFiltered.setRGB(x, y, ImageUtils.getPixel(iavg, iavg, iavg));
            }
        }
        return Pair.of(horizontalFiltered,floats);
    }

    public static void main(String[] args) throws IOException {
        String sourcePath = args[0];
        logger.info("source = {}", sourcePath);
        BufferedImage source = ImageIO.read(new FileInputStream(sourcePath));
        int W = source.getWidth();
        int H = source.getHeight();
        Frame srcFrame = new Frame(0, 0, W, H);
        logger.info("Source frame {}", srcFrame);

        Frame destFrame = new Frame(0, 0, FULL_HD_W, FULL_HD_H);

        logger.info("Dest frame {}", destFrame);

        Pair<Frame,Frame> horSplit = destFrame.splitHorizontal(0.5);
            Pair<Frame,Frame> v1Split = horSplit.getLeft().splitVertical(0.5);
            Pair<Frame,Frame> v2Split = horSplit.getRight().splitVertical(0.5);

        Frame frame1 = v1Split.getLeft().snapWithBorder(4).fit(srcFrame);
        logger.info("frame1 = {}", frame1);
        Frame frame2 = v1Split.getRight().snapWithBorder(4).fit(srcFrame);
        logger.info("frame2 = {}", frame2);
        Frame frame3 = v2Split.getLeft().snapWithBorder(4).fit(srcFrame);
        logger.info("frame3 = {}", frame3);
        Frame frame4 = v2Split.getRight().snapWithBorder(4).fit(srcFrame);
        logger.info("frame4 = {}", frame4);

        String destVideoFolder = "/bigdata/video.render";

        new File(destVideoFolder).mkdirs();

        int frame = 0;

        do {
            logger.info("Rendering frame # {}", frame1);
            BufferedImage dest = new BufferedImage(destFrame.width(), destFrame.height(), BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = (Graphics2D) dest.getGraphics();
                g2d.setRenderingHint(KEY_RENDERING, VALUE_RENDER_QUALITY);
                g2d.setRenderingHint(KEY_TEXT_ANTIALIASING, VALUE_TEXT_ANTIALIAS_OFF);
                g2d.setRenderingHint(KEY_ALPHA_INTERPOLATION, VALUE_ALPHA_INTERPOLATION_QUALITY);

            g2d.setColor(Color.WHITE);
            g2d.drawRect(0,0,destFrame.width()-1, destFrame.height()-1);

            BufferedImage image1 = new BufferedImage(W, H, BufferedImage.TYPE_INT_RGB);
            BufferedImage image2 = new BufferedImage(W, H, BufferedImage.TYPE_INT_RGB);

            for (int y = 0; y < H; y++) {
                for (int x = 0; x < W; x++) {
                    int color = source.getRGB(x, y);
                    int yVal = (int) (255.0 * ImageUtils.getYPixelDouble(color));
                    image1.setRGB(x, y, color);
                    image2.setRGB(x, y, ImageUtils.getPixel(yVal, yVal, yVal));
                }
            }

            Pair<BufferedImage,float[]> horizontal = horizontalFiltered(image2);

            g2d.drawImage(image1, frame1.x1, frame1.y1, frame1.width(), frame1.height(), null);
            g2d.drawImage(image2, frame2.x1, frame2.y1, frame2.width(), frame2.height(), null);
            g2d.drawImage(horizontal.getLeft(), frame3.x1, frame3.y1, frame3.width(), frame3.height(), null);
            //g2d.drawImage(image4, frame4.x1, frame4.y1, frame4.width(), frame4.height(), null);
            renderGraphic(g2d, frame4, horizontal.getRight());

            g2d.setFont(new Font("Courier", Font.BOLD, 32));
            FontMetrics fontMetrics = g2d.getFontMetrics();
            g2d.setColor(Color.WHITE);
            String s = String.format("Size: %d x %d px, Frame : %d", W,H,frame);
            g2d.drawString(s, 0 + fontMetrics.stringWidth(s), 32);

            ImageIO.write(dest, "PNG", new FileOutputStream(destVideoFolder + "/art-" + String.format("%05d.png", frame)));

            frame++;
        } while (frame < 1);
    }

    private static void renderGraphic(Graphics2D g2d, Frame frame, float[] values) {
        g2d.setColor(Color.WHITE);
        int x[] = new int[values.length];
        int y[] = new int[values.length];
        for (int i = 0; i < values.length; i++) {
            x[i] = frame.x1 + (int) (frame.width() * values[i]);
            y[i] = frame.y1 + (int) ((double) frame.height() * i / values.length);
        }
        g2d.drawPolyline(x, y, values.length);
        g2d.drawRect(frame.x1,frame.y1,frame.width(),frame.height());
    }

}
