package mayton.phototimesort;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

public class ImageVisitor implements FileVisitor<File> {
    @Override
    public FileVisitResult preVisitDirectory(File file, BasicFileAttributes basicFileAttributes) throws IOException {
        return null;
    }

    @Override
    public FileVisitResult visitFile(File file, BasicFileAttributes basicFileAttributes) throws IOException {
        return null;
    }

    @Override
    public FileVisitResult visitFileFailed(File file, IOException e) throws IOException {
        return null;
    }

    @Override
    public FileVisitResult postVisitDirectory(File file, IOException e) throws IOException {
        return null;
    }
}
