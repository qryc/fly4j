package fly4j.common.back;


import fly4j.common.file.zip.Zip4jTool;
import fly4j.common.test.util.TData;
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
import java.nio.file.Files;
import java.nio.file.Path;

import static fly4j.common.test.util.TData.tDataDirPath;
import static fly4j.common.test.util.TData.tPath;

/**
 * @author qryc
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class TestZip4jTool {
    static final Logger log = LoggerFactory.getLogger(TestZip4jTool.class);
    //压缩文件


    @Before
    public void setup() throws Exception {
        TData.deleteTestDir();
        //创建测试文件
        TData.createTestDefaultFiles();
    }

    @Test
    public void testZipUnzip() throws Exception {

        File zipFile = Path.of(tPath.toString(), "zip", "test.zip").toFile();
        File unZipDir = Path.of(tPath.toString(), "unzip").toFile();
        Assert.assertFalse(zipFile.exists());
        Assert.assertFalse(unZipDir.exists());

        Zip4jTool.zipDir(zipFile, tDataDirPath.toFile(), "123");
        Zip4jTool.unZip(zipFile, unZipDir, "123");

        Assert.assertEquals("个人资料保存目录", Files.readString(Path.of(unZipDir.getAbsolutePath(), "资料/readme.md")));
        Assert.assertEquals("java是面向对象语言", Files.readString(Path.of(unZipDir.getAbsolutePath(), "资料/Java/java语法.txt")));
        Assert.assertEquals("Spring框架IOC，AOP，支持MVC", Files.readString(Path.of(unZipDir.getAbsolutePath(), "资料/Java/Spring框架.txt")));
        Assert.assertEquals("窗前明月光", Files.readString(Path.of(unZipDir.getAbsolutePath(), "资料/李白/静夜思.txt")));
        Assert.assertTrue(Files.exists(Path.of(unZipDir.getAbsolutePath(), "资料/todo")));
    }

    @Test
    public void testZipUnzip2() throws Exception {
        File zipFile = Path.of(tPath.toString(), "zip", "test.zip").toFile();
        File unZipDir = Path.of(tPath.toString(), "unzip").toFile();
        File sourceDir = new File(tDataDirPath.toString() + "/");
        Zip4jTool.zipDir(zipFile, sourceDir, "123");
        Zip4jTool.unZip(zipFile, unZipDir, "123");

        Assert.assertEquals("个人资料保存目录", Files.readString(Path.of(unZipDir.getAbsolutePath(), "资料/readme.md")));
        Assert.assertEquals("java是面向对象语言", Files.readString(Path.of(unZipDir.getAbsolutePath(), "资料/Java/java语法.txt")));
        Assert.assertEquals("Spring框架IOC，AOP，支持MVC", Files.readString(Path.of(unZipDir.getAbsolutePath(), "资料/Java/Spring框架.txt")));
        Assert.assertEquals("窗前明月光", Files.readString(Path.of(unZipDir.getAbsolutePath(), "资料/李白/静夜思.txt")));
        Assert.assertTrue(Files.exists(Path.of(unZipDir.getAbsolutePath(), "资料/todo")));
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
        FileUtils.forceDelete(tPath.toFile());
    }


}
