package fly4b.back.zip;


import fly4j.common.back.DirCompareService;
import fly4j.common.back.DirCompareServiceImpl;
import fly4j.common.back.DirZipService;
import fly4j.common.back.ZipConfig;
import fly4j.common.back.zip.Zip4jTool;
import fly4j.common.lang.FlyResult;
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

import java.io.File;
import java.nio.file.Path;

import static fly4j.test.util.TestData.backDirPath;
import static fly4j.test.util.TestData.sourceDirPath;

/**
 * @author qryc
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class TestZip4jTool {
    static final Logger log = LoggerFactory.getLogger(TestZip4jTool.class);
    //压缩文件
    File zipFile = Path.of(backDirPath.toString(), "test.zip").toFile();
    //源文件夹
    File sourceDir = sourceDirPath.toFile();
    //压缩目录
    File backDir = backDirPath.toFile();

    @Before
    public void setup() throws Exception {
        TestData.createTestFiles();
    }

    @Test
    public void testZip1() throws Exception {
        Zip4jTool.zipDir(zipFile, sourceDir, "123");
        Zip4jTool.unZip(zipFile, backDir, "123");
        TestData.assertAllFiles(backDir);
    }

    @Test
    public void testZip2() throws Exception {
        //测试/结尾压缩
        Zip4jTool.zipDir(zipFile, new File(TestData.testBasePath.toString(), "sourcePath/"), "123");
        Zip4jTool.unZip(zipFile, backDir, "123");
        TestData.assertAllFiles(backDir);
    }

    @Test
    public void testZip3() throws Exception {
//        DirCompareService dirCompare = new DirCompareServiceImpl();
//        DirZipService dirZip = new DirZipService();
//        dirZip.setDirCompare(dirCompare);
//        ZipConfig zipConfig = new ZipConfig()
//                .setSourceDir(sourceDirPath.toFile())
//                .setDestZipFile(Path.of(backDirPath.toString(), "test.zip").toFile())
//                .setPassword("ab123");
//        FlyResult flyResult = dirZip.zipDirWithVerify(zipConfig);
//        System.out.println(flyResult.getMsg());
//        Assert.assertTrue(flyResult.isSuccess());
//        Zip4jTool.unZip(Path.of(backDirPath.toString(), "test.zip").toFile(), backDirPath.toFile(), "ab123");
//        TestData.assertAllFiles(backDir);
    }

    @After
    public void tearDown() throws Exception {
        FileUtils.forceDelete(TestData.testBasePath.toFile());
    }


}
