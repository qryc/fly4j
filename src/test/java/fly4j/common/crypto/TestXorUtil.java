package fly4j.common.crypto;

import fly4j.common.pesistence.file.FileStrStore;
import fly4j.test.util.TData;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Path;

public class TestXorUtil {
    public static void main(String[] args) throws Exception {
        XorUtil.xorFile2File(Path.of(TData.tPath.toString(), "xorSource.txt"), Path.of(TData.tPath.toString(), "xorTarget.fbk"), 123);
        XorUtil.xorFile2File(Path.of(TData.tPath.toString(), "xorTarget.fbk"), Path.of(TData.tPath.toString(), "xorResote.txt"), 123);
    }
    @Before
    public void setup() throws Exception {
        TData.createTestFiles();
    }

    @Test
    public void crypt() throws Exception {
        XorUtil.xorFile2File(Path.of(TData.sourceDirPath.toString(), "a.txt"), Path.of(TData.sourceDirPath.toString(), "aCrypt.txt"), 123);
        Assert.assertEquals("a中国", FileStrStore.getValue(Path.of(TData.sourceDirPath.toString(), "a.txt")));
        System.out.println(FileStrStore.getValue(Path.of(TData.sourceDirPath.toString(), "sourcePath/aCrypt.txt")));
        XorUtil.xorFile2File(Path.of(TData.sourceDirPath.toString(), "aCrypt.txt"), Path.of(TData.sourceDirPath.toString(), "aDcrypt.txt"), 123);
        Assert.assertEquals("a中国", FileStrStore.getValue(Path.of(TData.sourceDirPath.toString(), "aDcrypt.txt")));
    }
    @After
    public void tearDown() throws Exception {
        FileUtils.forceDelete(TData.tPath.toFile());
    }

}
