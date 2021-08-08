package fly4b.back.zip;

import fly4j.common.pesistence.file.FileStrStore;
import fly4j.common.test.TestData;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;

import java.nio.file.Files;
import java.nio.file.Path;

public class DirTestUtil {
    //源文件
    public static final Path sourcePath = Path.of(TestData.testPath.toString(), "sourcePath");
    //压缩文件目标文件夹
    public static final Path backPath = Path.of(TestData.testPath.toString(), "back");
    public static void createTestFiles() throws Exception {
        if (Files.exists(TestData.testPath))
            FileUtils.forceDelete(TestData.testPath.toFile());
        Assert.assertFalse(Files.exists(backPath));
        Files.createDirectories(backPath);
        Assert.assertTrue(Files.exists(backPath));

        //创建测试文件
        Assert.assertFalse(Files.exists(sourcePath));
        FileStrStore.setValue(Path.of(sourcePath.toString(), "a.txt"), "a中国");
        FileStrStore.setValue(Path.of(sourcePath.toString(), "b.txt"), "b中国");
        FileStrStore.setValue(Path.of(sourcePath.toString(), "c.txt"), "c中国");
        FileStrStore.setValue(Path.of(sourcePath.toString(), "childDir/aa.txt"), "aa中国");
        FileStrStore.setValue(Path.of(sourcePath.toString(), "childDir/bb.txt"), "bb中国");
    }

}
