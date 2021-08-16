package mayton;

import java.io.IOException;
import java.io.Reader;

public class CountingReader extends Reader {

    private Reader reader;

    private long cnt;

    public CountingReader(Reader reader) {
        this.reader = reader;
        this.cnt = 0;
    }

    @Override
    public int read(char[] chars, int i, int i1) throws IOException {
        int res=reader.read(chars, i, i1);
        cnt+=res;
        return res;
    }

    @Override
    public void close() throws IOException {

    }

    public long getCnt() {
        return cnt;
    }
}
