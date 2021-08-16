package mayton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.image.ImageObserver;

public class ImageObserverWaiter implements ImageObserver {

    static Logger logger = LoggerFactory.getLogger(ImageObserverWaiter.class);

    @Override
    public boolean imageUpdate(Image image, int i, int i1, int i2, int i3, int i4) {
        logger.info("imageUpdate ({} {} {} {} {}})", i, i1, i2, i3, i4);
        return false;
    }
}
