package fly4j.common.test.util;

import fly4j.common.os.OsUtil;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.core.util.Assert;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by qryc on 2020/3/15.
 */
public class TData {
    public static Path readOnlyTestPath;
    //测试数据目录 userHome/flyDataTest
    public static Path tPath;
    //源文件 userHome/flyDataTest/sourcePath
    public static Path tDataDirPath;


    static {
        if (OsUtil.isWindows()) {
            tPath = Path.of("D:/flyDataTest");
        } else {
            tPath = Path.of(System.getProperty("user.home"), "flyDataTest");
            readOnlyTestPath=Path.of("/Volumes/HomeWork/FlyCode/FlyDoc/TestFile/");
        }
        tDataDirPath = tPath.resolve("资料");
        System.out.println("sourceDirPath:" + tDataDirPath);
    }

    public static void createTestDefaultFiles() throws IOException {
        Files.createDirectories(tDataDirPath.resolve("Java"));
        Files.createDirectories(tDataDirPath.resolve("李白"));
        Files.writeString(tDataDirPath.resolve("readme.md"), "个人资料保存目录");
        Files.writeString(tDataDirPath.resolve("Java/java语法.txt"), "java是面向对象语言");
        Files.writeString(tDataDirPath.resolve("Java/Spring框架.txt"), "Spring框架IOC，AOP，支持MVC");
        Files.writeString(tDataDirPath.resolve("李白/静夜思.txt"), "窗前明月光");
        Files.createDirectories(tDataDirPath.resolve("todo"));
    }

    public static void deleteTestDir() throws IOException {
        //删除并重新创建测试目录
        System.out.println("deleteTestDir in:" + tPath);
        if (Files.exists(tPath))
            FileUtils.forceDelete(tPath.toFile());

        assert (!Files.exists(tDataDirPath));
    }

    public static void setup() throws Exception {
        //删除测试文件
        deleteTestDir();
        //创建测试文件
        createTestDefaultFiles();
    }

    public static void tearDown() throws Exception {
        FileUtils.forceDelete(TData.tPath.toFile());
    }

}
