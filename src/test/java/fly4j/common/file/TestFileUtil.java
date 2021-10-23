package fly4j.common.file;

import fly4j.common.os.OsUtil;
import fly4j.common.test.util.TData;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import java.io.File;
import java.nio.file.Path;

/**
 * @author qryc
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class TestFileUtil {


    @Before
    public void setup() throws Exception {
        TData.createTestFiles();
    }


    @Test
    public void testFile() throws Exception {
//        System.out.println(TestData.tTargetDir);
//        FileUtil.delDir(FilenameUtils.concat(TestData.tTargetDir, "TestFileUtil"));
//        Assert.assertTrue(!new File(FilenameUtils.concat(TestData.tTargetDir, "TestFileUtil")).exists());
//        FileUtil.forceMkdir(FilenameUtils.concat(TestData.tTargetDir, "TestFileUtil/a/aa/aaa"));
//        Assert.assertTrue(new File(FilenameUtils.concat(TestData.tTargetDir, "TestFileUtil/a/aa/aaa")).exists());
    }

    @Test
    public void testFileSub() throws Exception {
        //toRealPath 要求文件必须存在，toAbsolutePath不需要
        System.out.println(Path.of("/../a.txt"));
        System.out.println(Path.of("/../a.txt").toAbsolutePath());
        System.out.println(Path.of("/../a.txt").normalize());
        System.out.println(new File("/../a.txt").getAbsolutePath());
//        System.out.println(Path.of("D:\\flyNetDik\\admin/../admin\\backData").toRealPath());
//
//        System.out.println(Path.of("/../a.txt").toRealPath());
    }

    /**
     * ***************************下边测试部分演示容易出错部分用法***************************
     */
    public static void main(String[] args) throws Exception {

//        String pathStr = "D:\\back\\a.zip";
//
//        String v = getMd5ByFile(new FileInputStream(pathStr));
//        System.out.println("MD5:" + v.toUpperCase());
//        pathStr = "D:\\back\\b.zip";
//        v = getMd5ByFile(new FileInputStream(pathStr));
//        System.out.println("MD5:" + v.toUpperCase());

        //System.out.println("MD5:"+DigestUtils.md5Hex("WANGQIUYUN"));
//        System.out.println(convertK2M(18000000000L));
//        System.out.println(convertK2M(18000000000L));
//        System.out.println(convertK2M(1073741824));
//        System.out.println(convertM2K(19));
//        FileUtil.rename(new File("D:\\ssssss\\b.jpg"),"c");
//        testFilePath();

        testFileName();


    }

    @Test
    public void getSubPathUnix() throws Exception {
        var key = FilenameUtil.getSubPathUnix("/export/资料/文件/a.txt", "/export/资料/");
        Assert.assertEquals("文件/a.txt", key);
        key = FilenameUtil.getSubPathUnix("/export/资料/文件/a.txt", "/export/资料");
        Assert.assertEquals("文件/a.txt", key);
        key = FilenameUtil.getSubPathUnix("D:\\资料\\文件\\a.txt", "D:\\资料");
        Assert.assertEquals("文件/a.txt", key);
        key = FilenameUtil.getSubPathUnix("D:\\文件\\a.txt", "D:");
        Assert.assertEquals("文件/a.txt", key);
        key = FilenameUtil.getSubPathUnix("/export/资料/文件", "/export/资料/文件");
        Assert.assertEquals("", key);
        key = FilenameUtil.getSubPathUnix("/export/资料/文件", "/export/资料/文件/");
        Assert.assertEquals("", key);
        key = FilenameUtil.getSubPathUnix("/export/资料/文件/", "/export/资料/文件/");
        Assert.assertEquals("", key);
        key = FilenameUtil.getSubPathUnix("/export/资料/文件/a", "/export/资料/文件/");
        Assert.assertEquals("a", key);
        key = FilenameUtil.getSubPathUnix("/export/资料/文件/a", "/export/资料/文件");
        Assert.assertEquals("a", key);
        key = FilenameUtil.getSubPathUnix("/export/资料/文件/a/", "/export/资料/文件/");
        Assert.assertEquals("a", key);


    }
    @Test
    public void getSubPathUnix2() throws Exception {
        var key = FilenameUtil.getSubPathUnix2("/export/资料/文件/a.txt", "/export/资料/");
        Assert.assertEquals("文件/a.txt", key);
        key = FilenameUtil.getSubPathUnix2("/export/资料/文件/a.txt", "/export/资料");
        Assert.assertEquals("文件/a.txt", key);
        if(OsUtil.isWindows()){
            key = FilenameUtil.getSubPathUnix2("D:\\资料\\文件\\a.txt", "D:\\资料");
            Assert.assertEquals("文件/a.txt", key);
            key = FilenameUtil.getSubPathUnix2("D:\\文件\\a.txt", "D:");
            Assert.assertEquals("文件/a.txt", key);
        }

        key = FilenameUtil.getSubPathUnix2("/export/资料/文件", "/export/资料/文件");
        Assert.assertEquals("", key);
        key = FilenameUtil.getSubPathUnix2("/export/资料/文件", "/export/资料/文件/");
        Assert.assertEquals("", key);
        key = FilenameUtil.getSubPathUnix2("/export/资料/文件/", "/export/资料/文件/");
        Assert.assertEquals("", key);
        key = FilenameUtil.getSubPathUnix2("/export/资料/文件/a", "/export/资料/文件/");
        Assert.assertEquals("a", key);
        key = FilenameUtil.getSubPathUnix2("/export/资料/文件/a", "/export/资料/文件");
        Assert.assertEquals("a", key);
        key = FilenameUtil.getSubPathUnix2("/export/资料/文件/a/", "/export/资料/文件/");
        Assert.assertEquals("a", key);


    }
    @Test
    public void deleteOneRepeatFile() throws Exception {
        File fileA = Path.of(TData.tDataDirPath.toString(), "readme.md").toFile();
        File fileACopy = Path.of(TData.tDataDirPath.toString(), "readmeCopy.md").toFile();
        Assert.assertEquals(true,fileA.exists());

        //删除相同同一个文件
        FileUtil.deleteRepeatFile(fileA, fileA);
        Assert.assertEquals(true,fileA.exists());
        FileUtil.deleteRepeatFile(fileA, new File(fileA.getAbsolutePath()));
        Assert.assertEquals(true,fileA.exists());

        //删除不同文件
        File fileB = Path.of(TData.tDataDirPath.toString(), "b.txt").toFile();
        FileUtil.deleteRepeatFile(fileA, fileB);
        Assert.assertEquals(true,fileA.exists());

        //删除重复文件
        Assert.assertEquals(false,fileACopy.exists());
        FileUtils.copyFile(fileA,fileACopy);
        Assert.assertEquals(true,fileACopy.exists());
        FileUtil.deleteRepeatFile(fileA, fileACopy);
        Assert.assertEquals(false,fileA.exists());
    }

    private static void testFilePath() {
        //测试路径
        StringBuilder msg = new StringBuilder();
        msg.append("System.getProperty(\"user.dir\")").append(":").append(System.getProperty("user.dir")).append(StringUtils.LF);
        msg.append(" System.getProperty(\"java.class.pathStr\")").append(":").append(System.getProperty("java.class.pathStr")).append(StringUtils.LF);
        //不可以在jar包使用
        msg.append("this.getClass().getResource(\"/\").getPath()").append(":").append(FileUtil.class.getResource("/").getPath()).append(StringUtils.LF);
        System.out.println(msg);
    }

    private static void testFileName() {
        //fly\pin  warnig:会丢失 blogs,因为不知道结尾是文件，还是文件夹
        System.out.println(FilenameUtils.getPathNoEndSeparator("D:\\fly\\pin\\blogs"));
        //D:\fly\pin
        System.out.println(FilenameUtils.getFullPathNoEndSeparator("D:\\fly\\pin\\blogs"));
        //blogs
        System.out.println(FilenameUtils.getBaseName("D:\\fly\\pin\\blogs"));
        //空
        System.out.println(FilenameUtils.getBaseName("D:\\fly\\pin\\blogs\\"));
        //pin warnig:会丢失 blogs
        System.out.println(FilenameUtils.getBaseName(FilenameUtils.getPathNoEndSeparator("D:\\fly\\pin\\blogs")));
    }

    @After
    public void tearDown() throws Exception {
        FileUtils.forceDelete(TData.tPath.toFile());
    }

}
