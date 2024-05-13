package jdk11;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Set;

public class Jdk11IO {
    public static void main(String[] args) throws IOException {
        Path.of("a", "b").toFile();
//        Set.of(StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING,
//                StandardOpenOption.WRITE);
        Path txtFilePath=Path.of("/export/a/a1/a2", "a.txt");
        System.out.println(Files.exists(txtFilePath));
        Files.createDirectories(txtFilePath.getParent());
        Files.createDirectory(txtFilePath);
        Files.writeString(txtFilePath, "abc", StandardCharsets.UTF_8);
        System.out.println(Files.readString(Path.of("/export/a/a1/a2", "a.txt")));
//
//        System.out.println(Charset.forName("utf-8"));
//        System.out.println(StandardCharsets.UTF_8);

        //中间目录和目录分开函数
//        Files.createDirectory(Path.of(""));
//        Files.createDirectories(Path.of("/export/a/a1/a2"));
//
//        var classLoader = ClassLoader.getSystemClassLoader();
//        var inputStream = classLoader.getResourceAsStream("javastack.txt");
//        var javastack = File.createTempFile("javastack2", "txt");
//        try (var outputStream = new FileOutputStream(javastack)) {
//            inputStream.transferTo(outputStream);
//        }
    }
}
