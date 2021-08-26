package fly4j.common.crypto;

import fly4j.common.pesistence.file.FileStrStore;
import fly4j.test.util.TestData;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Path;

public class TestXorUtil {
    public static void main(String[] args) throws Exception {
        XorUtil.xorFile2File(Path.of(TestData.testBasePath.toString(), "xorSource.txt"), Path.of(TestData.testBasePath.toString(), "xorTarget.fbk"), 123);
        XorUtil.xorFile2File(Path.of(TestData.testBasePath.toString(), "xorTarget.fbk"), Path.of(TestData.testBasePath.toString(), "xorResote.txt"), 123);
    }
    @Before
    public void setup() throws Exception {
        TestData.createTestFiles();
    }

    @Test
    public void crypt() throws Exception {
        TestData.assertAllSourceFiles();
        XorUtil.xorFile2File(Path.of(TestData.sourceDirPath.toString(), "a.txt"), Path.of(TestData.sourceDirPath.toString(), "aCrypt.txt"), 123);
        Assert.assertEquals("a中国", FileStrStore.getValue(Path.of(TestData.sourceDirPath.toString(), "a.txt")));
        System.out.println(FileStrStore.getValue(Path.of(TestData.sourceDirPath.toString(), "sourcePath/aCrypt.txt")));
        XorUtil.xorFile2File(Path.of(TestData.sourceDirPath.toString(), "aCrypt.txt"), Path.of(TestData.sourceDirPath.toString(), "aDcrypt.txt"), 123);
        Assert.assertEquals("a中国", FileStrStore.getValue(Path.of(TestData.sourceDirPath.toString(), "aDcrypt.txt")));
    }
    @After
    public void tearDown() throws Exception {
        FileUtils.forceDelete(TestData.testBasePath.toFile());
    }

}
