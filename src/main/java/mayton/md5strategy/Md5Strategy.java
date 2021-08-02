package mayton.md5strategy;

import javax.annotation.Nonnull;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;

public interface Md5Strategy {

    @Nonnull String extractMd5(@Nonnull Path file) throws IOException;

    void storeMd5(@Nonnull Path file,@Nonnull String md5) throws IOException;

}
