package fly4j.test.util;

import fly4j.common.os.OsUtil;
import fly4j.common.pesistence.file.FileStrStore;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.junit.Assert;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by qryc on 2020/3/15.
 */
public class TestData {
    //测试数据目录 userHome/flyDataTest
    public static Path testBasePath;
    //源文件 userHome/flyDataTest/sourcePath
    public static Path sourceDirPath;
    //压缩文件目标文件夹 userHome/flyDataTest/back
    public static Path backDirPath;


    static {
        if (OsUtil.isWindows()) {
            testBasePath = Path.of("D:/flyDataTest");
        } else {
            testBasePath = Path.of(System.getProperty("user.home"), "flyDataTest");
        }
        backDirPath = Path.of(testBasePath.toString(), "back");
        sourceDirPath = Path.of(testBasePath.toString(), "sourcePath");
    }


    public static void createTestFiles() throws Exception {
        //删除并重新创建测试目录
        System.out.println("createTestFiles in:" + testBasePath);
        if (Files.exists(testBasePath))
            FileUtils.forceDelete(testBasePath.toFile());
        Assert.assertFalse(Files.exists(backDirPath));
        Files.createDirectories(backDirPath);
        Assert.assertTrue(Files.exists(backDirPath));

        //创建测试文件
        Assert.assertFalse(Files.exists(sourceDirPath));
        FileStrStore.setValue(Path.of(sourceDirPath.toString(), "a.txt"), "a中国");
        FileStrStore.setValue(Path.of(sourceDirPath.toString(), "b.txt"), "b中国");
        FileStrStore.setValue(Path.of(sourceDirPath.toString(), "c.txt"), "c中国");
        FileStrStore.setValue(Path.of(sourceDirPath.toString(), "childDir/aa.txt"), "aa中国");
        FileStrStore.setValue(Path.of(sourceDirPath.toString(), "childDir/bb.txt"), "bb中国");
    }

    public static void assertAllSourceFiles() {
        assertAllFiles(TestData.testBasePath.toFile());
    }

    public static void assertAllFiles(File checkDir) {
        Assert.assertEquals("a中国", FileStrStore.getValue(Path.of(checkDir.getAbsolutePath(), "sourcePath/a.txt")));
        Assert.assertEquals("b中国", FileStrStore.getValue(Path.of(checkDir.getAbsolutePath(), "sourcePath/b.txt")));
        Assert.assertEquals("c中国", FileStrStore.getValue(Path.of(checkDir.getAbsolutePath(), "sourcePath/c.txt")));
        Assert.assertEquals("aa中国", FileStrStore.getValue(Path.of(checkDir.getAbsolutePath(), "sourcePath/childDir/aa.txt")));
        Assert.assertEquals("bb中国", FileStrStore.getValue(Path.of(checkDir.getAbsolutePath(), "sourcePath/childDir/bb.txt")));
    }
}
