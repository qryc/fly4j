package fly4j.test.util;

import fly4j.common.os.OsUtil;
import fly4j.common.pesistence.file.FileStrStore;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by qryc on 2020/3/15.
 */
public class TData {
    //测试数据目录 userHome/flyDataTest
    public static Path tPath;
    //源文件 userHome/flyDataTest/sourcePath
    public static Path sourceDirPath;


    static {
        if (OsUtil.isWindows()) {
            tPath = Path.of("D:/flyDataTest");
        } else {
            tPath = Path.of(System.getProperty("user.home"), "flyDataTest");
        }
        sourceDirPath = Path.of(tPath.toString(), "资料");
        System.out.println("sourceDirPath:" + sourceDirPath);
    }


    public static void createTestFiles() throws Exception {
        //删除并重新创建测试目录
        System.out.println("createTestFiles in:" + tPath);
        if (Files.exists(tPath))
            FileUtils.forceDelete(tPath.toFile());


        //创建测试文件
        Assert.assertFalse(Files.exists(sourceDirPath));
        FileStrStore.setValue(Path.of(sourceDirPath.toString(), "readme.md"), "个人资料保存目录");
        FileStrStore.setValue(Path.of(sourceDirPath.toString(), "Java/java语法.txt"), "java是面向对象语言");
        FileStrStore.setValue(Path.of(sourceDirPath.toString(), "Java/Spring框架.txt"), "Spring框架IOC，AOP，支持MVC");
        FileStrStore.setValue(Path.of(sourceDirPath.toString(), "李白/静夜思.txt"), "窗前明月光");
        Files.createDirectories(Path.of(sourceDirPath.toString(), "todo"));

    }


}
