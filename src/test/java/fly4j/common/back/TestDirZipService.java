package fly4j.common.back;


import fly4j.common.back.check.DirCompareService;
import fly4j.common.back.check.DirZipService;
import fly4j.common.file.FileAndDirFilter;
import fly4j.test.util.TestData;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author qryc
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class TestDirZipService {
    static final Logger log = LoggerFactory.getLogger(TestDirZipService.class);

    public TestDirZipService() {
        FileAndDirFilter fileAndDirFilter = new FileAndDirFilter();
//        fileAndDirFilter.setFilterDirNames(Set.of(""));
        DirCompareService dirCompareService = new DirCompareService();
//        dirCompareService.setNoNeedCalMd5FileFilter(fileAndDirFilter);
    }

    @Before
    public void setup() throws Exception {
        TestData.createTestFiles();
    }

    @Test
    public void zipDirWithVerify() throws Exception {
//        var zipFilePath = Path.of(backDirPath.toString(), OsUtil.getSimpleOsName() + DateUtil.getHourStr4Name(new Date()) + ".zip");
//        var zipConfig = new ZipConfig()
//                .setSourceDir(sourceDirPath.toFile())
//                .setDestZipFile(zipFilePath.toFile())
//                .setPassword("123")
//                .setDelZip(false);
//        var flyResult = dirZipService.zipDirWithVerify(zipConfig);
//        System.out.println(flyResult.getMsg());

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
