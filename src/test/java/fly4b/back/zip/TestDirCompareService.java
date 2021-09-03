package fly4b.back.zip;


import fly4j.common.back.*;
import fly4j.common.file.FileAndDirFilter;
import fly4j.common.lang.DateUtil;
import fly4j.common.os.OsUtil;
import fly4j.common.pesistence.file.FileStrStore;
import fly4j.test.util.TestData;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.Date;

import static fly4j.test.util.TestData.backDirPath;
import static fly4j.test.util.TestData.sourceDirPath;

/**
 * @author qryc
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class TestDirCompareService {
    static final Logger log = LoggerFactory.getLogger(TestDirCompareService.class);
    private DirCompareService dirCompareService;

    public TestDirCompareService() {
        FileAndDirFilter fileAndDirFilter = new FileAndDirFilter();
//        fileAndDirFilter.setFilterDirNames(Set.of(""));
        DirCompareServiceImpl dirCompareServiceImpl = new DirCompareServiceImpl();
        dirCompareServiceImpl.setNoNeedCalMd5FileFilter(fileAndDirFilter);
        dirCompareServiceImpl.setCheckEmptyDir(true);
        dirCompareService = dirCompareServiceImpl;
    }

    @Before
    public void setup() throws Exception {
        FileUtils.deleteQuietly(TestData.testBasePath.toFile());
        TestData.createTestFiles();
    }

    @Test
    public void genDirMd5VersionTagLen() throws Exception {
        if(OsUtil.isWindows()){
            return;
        }
        String md5 = """
                {"":"dir","/childDir":"dir","/childDir/aa.txt":"8","/childDir/bb.txt":"8","/c.txt":"7","/b.txt":"7","/a.txt":"7"}""";
        var zipFilePath = Path.of(backDirPath.toString(), OsUtil.getSimpleOsName() + DateUtil.getHourStr4Name(new Date()) + ".zip");
        var zipConfig = new ZipConfig()
                .setSourceDir(sourceDirPath.toFile())
                .setDestZipFile(zipFilePath.toFile())
                .setPassword("123")
                .setDelZip(false);
        Path md5StorePath = Path.of(sourceDirPath.toString(), DirCompareService.getDefaultVersionFileName(sourceDirPath.toString()));
        String md5FileStr = dirCompareService.genDirMd5VersionTag(sourceDirPath.toFile(), md5StorePath, VersionType.LEN);
        Assert.assertEquals(md5, FileStrStore.getValue(Path.of(md5FileStr)));
    }

    @Test
    public void genDirMd5VersionTagMd5() throws Exception {
        if(OsUtil.isWindows()){
            return;
        }
        String md5 = """
                {"":"dir","/childDir":"dir","/childDir/aa.txt":"ce4f75647b15fc7fa4f01ad9f856d307","/childDir/bb.txt":"35cb9fa3d1b1d570a7a64c7d27b4ac27","/c.txt":"29fbb78de8005a02cc22a2550c383745","/b.txt":"f0a408d9c5b8e4b888385a6c630beba4","/a.txt":"c173b145b212ca55558eba13aac59aa3"}""";
        var zipFilePath = Path.of(backDirPath.toString(), OsUtil.getSimpleOsName() + DateUtil.getHourStr4Name(new Date()) + ".zip");
        var zipConfig = new ZipConfig()
                .setSourceDir(sourceDirPath.toFile())
                .setDestZipFile(zipFilePath.toFile())
                .setPassword("123")
                .setDelZip(false);
        Path md5StorePath = Path.of(sourceDirPath.toString(), DirCompareService.getDefaultVersionFileName(sourceDirPath.toString()));
        String md5FileStr = dirCompareService.genDirMd5VersionTag(sourceDirPath.toFile(), md5StorePath, VersionType.MD5);
        Assert.assertEquals(md5, FileStrStore.getValue(Path.of(md5FileStr)));
    }

    @After
    public void tearDown() throws Exception {
//        FileUtils.forceDeleteOnExit(TestData.testPath.toFile());
    }


}
