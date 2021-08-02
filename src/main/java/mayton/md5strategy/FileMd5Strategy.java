package mayton.md5strategy;

import org.apache.commons.io.IOUtils;

import javax.annotation.Nonnull;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

public class FileMd5Strategy implements Md5Strategy {

    @Nonnull
    @Override
    public String extractMd5(@Nonnull Path file) throws IOException {
        return IOUtils.toString(new FileReader(file.toAbsolutePath() + ".md5"));
    }

    @Override
    public void storeMd5(@Nonnull Path file, @Nonnull String md5) throws IOException {
        FileWriter fw = new FileWriter(file.toAbsolutePath() + ".md5");
        fw.write(md5);
        fw.close();
    }
}
