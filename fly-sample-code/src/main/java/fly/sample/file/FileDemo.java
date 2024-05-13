package fly.sample.file;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by qryc on 2021/9/17
 */
public class FileDemo {
    public static void main(String[] args) throws Exception {

//        testIsFileIsDirectory();

//        Files.createDirectory(Path.of("/test"));
//        Files.writeString(Path.of("/test/不存在的文件.txt"), "testtest");
//        System.out.println("isDirectory:" + file.isDirectory());
//        System.out.println("isFile:" + file.isFile());
//        Files.deleteIfExists(Path.of("/test/不存在的文件.txt"));
//        renameToExists();
//
//        moveToExists();
        delete();
    }

    public static void delete() throws Exception {

        FileUtils.deleteDirectory(Path.of(System.getProperty("user.home"), "test").toFile());
        Path pathA = Path.of(System.getProperty("user.home"), "test", "a.txt");
        Files.createDirectories(pathA.getParent());
        Files.createFile(pathA);
        Files.deleteIfExists(Path.of(System.getProperty("user.home"), "test"));
        System.out.println("pathA exists:" + Files.exists(pathA));


    }

    public static void renameToExists() throws Exception {

        FileUtils.deleteDirectory(Path.of(System.getProperty("user.home"), "test").toFile());
        Path pathA = Path.of(System.getProperty("user.home"), "test", "a.txt");
        Files.createDirectories(pathA.getParent());
        Files.createFile(pathA);
        Path pathB = Path.of(System.getProperty("user.home"), "test", "b.txt");
        Files.createFile(pathB);
        pathA.toFile().renameTo(pathB.toFile());
        System.out.println("pathA exists:" + Files.exists(pathA));
        System.out.println("pathB exists:" + Files.exists(pathB));


    }

    public static void moveToExists() throws IOException {

        FileUtils.deleteDirectory(Path.of(System.getProperty("user.home"), "test").toFile());
        Path pathA = Path.of(System.getProperty("user.home"), "test", "a.txt");
        Files.createDirectories(pathA.getParent());
        Files.createFile(pathA);
        Path pathB = Path.of(System.getProperty("user.home"), "test", "b.txt");
        Files.createFile(pathB);
        Files.move(pathA, pathB);
        System.out.println("pathA exists:" + Files.exists(pathA));
        System.out.println("pathB exists:" + Files.exists(pathB));


    }


    private static void testIsFileIsDirectory() throws IOException {
        File file = new File("/test/不存在的文件.txt");
        Files.deleteIfExists(Path.of("/test/不存在的文件.txt"));
        System.out.println("isDirectory:" + file.isDirectory());
        System.out.println("isFile:" + file.isFile());
        System.out.println("isDirectoryByPath:" + Files.isDirectory(Path.of("/test/不存在的文件.txt")));
    }
}
