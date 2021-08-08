package fly4b.back.zip;


import fly4j.common.back.DirCompareService;
import fly4j.common.back.DirCompareServiceImpl;
import fly4j.common.back.DirZipService;
import fly4j.common.back.ZipConfig;
import fly4j.common.back.zip.Zip4jTool;
import fly4j.common.file.FileAndDirFilter;
import fly4j.common.lang.DateUtil;
import fly4j.common.lang.FlyResult;
import fly4j.common.os.OsUtil;
import fly4j.common.pesistence.file.FileStrStore;
import fly4j.common.test.TestData;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Path;
import java.util.Date;
import java.util.Set;

import static fly4b.back.zip.DirTestUtil.backPath;
import static fly4b.back.zip.DirTestUtil.sourcePath;

/**
 * @author qryc
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class TestDirZipService {
    static final Logger log = LoggerFactory.getLogger(TestDirZipService.class);
    private DirZipService dirZipService;

    public TestDirZipService() {
        FileAndDirFilter fileAndDirFilter = new FileAndDirFilter();
//        fileAndDirFilter.setFilterDirNames(Set.of(""));
        DirCompareServiceImpl dirCompareService = new DirCompareServiceImpl();
        dirCompareService.setNoNeedCalMd5FileFilter(fileAndDirFilter);
        dirZipService = new DirZipService();
        dirZipService.setDirCompare(dirCompareService);
    }

    @Before
    public void setup() throws Exception {
        DirTestUtil.createTestFiles();
    }

    @Test
    public void zipDirWithVerify() throws Exception {
        var zipFilePath = Path.of(backPath.toString(), OsUtil.getSimpleOsName() + DateUtil.getHourStr4Name(new Date()) + ".zip");
        var zipConfig = new ZipConfig()
                .setBeZipSourceDir(sourcePath.toFile())
                .setDestZipFile(zipFilePath.toFile())
                .setPassword("123")
                .setDelZip(false);
        var flyResult = dirZipService.zipDirWithVerify(zipConfig);
        System.out.println(flyResult.getMsg());

//        Zip4jTool.zipDir(Path.of(backPath.toString(), "test.zip").toFile(), sourcePath.toFile(), "123");
//
//        Zip4jTool.unZip(Path.of(backPath.toString(), "test.zip").toFile(), backPath.toFile(), "123");
//        Assert.assertEquals("a中国", FileStrStore.getValue(Path.of(backPath.toString(), "sourcePath/a.txt")));
//        Assert.assertEquals("b中国", FileStrStore.getValue(Path.of(backPath.toString(), "sourcePath/b.txt")));
//        Assert.assertEquals("c中国", FileStrStore.getValue(Path.of(backPath.toString(), "sourcePath/c.txt")));
//        Assert.assertEquals("aa中国", FileStrStore.getValue(Path.of(backPath.toString(), "sourcePath/childDir/aa.txt")));
//        Assert.assertEquals("bb中国", FileStrStore.getValue(Path.of(backPath.toString(), "sourcePath/childDir/bb.txt")));
    }

    @After
    public void tearDown() throws Exception {
//        FileUtils.forceDeleteOnExit(TestData.testPath.toFile());
    }


}
