package mayton.md5strategy;

import javax.annotation.Nonnull;
import java.nio.file.Path;

public class XattrMd5Strategy implements Md5Strategy {

    @Nonnull
    @Override
    public String extractMd5(@Nonnull Path file) {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public void storeMd5(@Nonnull Path file, @Nonnull String md5) {
        throw new RuntimeException("Not implemented yet");
    }
}
