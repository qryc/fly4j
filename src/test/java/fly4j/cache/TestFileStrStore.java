package fly4j.cache;

import fly4j.common.cache.FlyCache;
import fly4j.common.cache.impl.FlyCacheJVM;
import fly4j.common.os.OsUtil;
import fly4j.common.pesistence.file.FileStrStore;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

public class TestFileStrStore {


    @Test
    public void testCache() throws Exception {
        var rootPath = OsUtil.isMac() ? "~" : "d:";
        FileUtils.forceDeleteOnExit(Path.of(rootPath,"aa").toFile());
        FileStrStore.setValue(Path.of(rootPath,"aa/aa.txt"), "abc");
        Assert.assertEquals("abc", FileStrStore.getValue(Path.of(rootPath,"aa/aa.txt")));

    }


}
