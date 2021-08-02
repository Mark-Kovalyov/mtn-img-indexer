package mayton;

import mayton.html.HtmlWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Locale;
import java.util.Stack;
import java.util.UUID;

import static mayton.ImageIndexer.INDEX_HTML;
import static mayton.ImageUtils.resizeImage;

public class JpegVisitor extends SimpleFileVisitor<Path> {

    static Logger logger = LoggerFactory.getLogger(JpegVisitor.class);

    double scale = 0.1;

    private Stack<HtmlWriter> htmlWriters = new Stack<>();

    public JpegVisitor() {

    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        logger.debug("pre visit dir {}", dir);
        HtmlWriter htmlWriter = new HtmlWriter(new FileWriter(dir.toString() + "/" + INDEX_HTML));
        htmlWriter.writeH1(dir.toString());
        htmlWriters.push(htmlWriter);
        return FileVisitResult.CONTINUE;
    }



    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        logger.debug("visit {}", file);
        String fileName = file.getFileName().toString().toLowerCase(Locale.ROOT);
        if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
            HtmlWriter htmlWriter = this.htmlWriters.peek();
            logger.info("processing jpeg file {}", file.toString());
            BufferedImage image = ImageIO.read(file.toFile());
            int x = image.getWidth();
            int y = image.getHeight();
            BufferedImage thumbnail = resizeImage(image, (int) (x * scale), (int) (y * scale));
            OutputStream outputStream = new FileOutputStream("/dev/null");
            ImageIO.write(thumbnail, "JPEG", outputStream);
            outputStream.close();
            //htmlWriter.writeImg(file.toString(), "width:100%;");
        }
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
        logger.debug("post visit");
        try {
            htmlWriters.pop().close();
        } catch (Exception e) {
            logger.warn("!", e);
        }
        return FileVisitResult.CONTINUE;
    }
}
