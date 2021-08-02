package mayton.xattr;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.FileStore;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.UserDefinedFileAttributeView;

public class Xattr {

    public static void main(String[] args) throws IOException {
        Path path = Paths.get("/storage/git.c");

// you should get the filestore for the path
        FileStore fileStore = Files.getFileStore(path);
        System.out.println("fileStore     : " + fileStore);

// check if the filesystem supports xattr
//
// I found out that the reported state might be wrong for ext3/ext4
//
// if you create following properties file it will be correct
// echo "ext4=user_attr" > $JAVA_HOME/lib/fstypes.properties
//
// keep in mind that it's possible that your ext3/ext4 does not support xattr
// might be related to: kernel configuration, mount options, etc.
        boolean supportsXattr = fileStore.supportsFileAttributeView(UserDefinedFileAttributeView.class);
        System.out.println("supports xattr: " + supportsXattr);

// get the file attribute view
        UserDefinedFileAttributeView view = Files.getFileAttributeView(path, UserDefinedFileAttributeView.class);
        String xattrName = "xattr-foo";
        String xattrValue = "dummy-value";

        Charset defaultCharset = Charset.defaultCharset();
        if (view.list().contains(xattrName)) {
            // get the size of the attribute value
            int xattrSize = view.size(xattrName);
            ByteBuffer buffer = ByteBuffer.allocateDirect(xattrSize);

            // read the attribute value
            int bytesRead = view.read(xattrName, buffer);

            // decode the buffer and print it
            buffer.flip();
            xattrValue = defaultCharset.decode(buffer).toString();
            System.out.println("xattr name    : " + xattrName);
            System.out.println("xattr value   : " + xattrValue);
        } else {
            // edit: System.out.println to System.out.printf in the next line
            System.out.printf("file has no xattr [%s]%n", xattrName);
        }

// write the current value back reversed, can be checked next run
// or on command line
        String newXattrValue = new StringBuilder(xattrValue).reverse().toString();
        view.write(xattrName, defaultCharset.encode(newXattrValue));
    }

}
