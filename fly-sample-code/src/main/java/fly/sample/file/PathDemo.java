package fly.sample.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by qryc on 2021/9/16
 */
public class PathDemo {
    public static void main(String[] args) throws IOException {
        Path dirPath = Path.of("/export", "test");
        System.out.println(dirPath);
        Path filePath = dirPath.resolve("a.txt");
        System.out.println(filePath);
        //得到相对路径
        System.out.println(dirPath.relativize(filePath));
        System.out.println(Path.of("D:\\资料\\文件").relativize(Path.of("D:\\资料\\文件\\a.txt")));
//        createDir();
    }

    private static void createDir() throws IOException {
        Path dirPath = Path.of("/export", "test");
        Files.createDirectories(dirPath);
        Files.createDirectories(dirPath);
    }
}
