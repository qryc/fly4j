package fly4j.common.back;

import fly4j.common.back.compare.DirCompareService;
import fly4j.common.file.FileUtil;
import fly4j.common.pesistence.file.FileStrStore;
import fly4j.test.util.TData;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

/**
 * @author qryc
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class TestDirCompreService {


    @Before
    public void setup() throws Exception {
        TData.createTestFiles();
    }

    @Test
    public void deleteOneRepeatFile() throws Exception {
        /**模拟新资料在历史版本上做了修改**/
        File testDataDir = new File(TData.tDataDirPath.toString());
        File historyDataDir = Path.of(TData.tPath.toString(), "历史资料").toFile();

        //新资料加入新文件
        FileUtils.copyDirectoryToDirectory(testDataDir, historyDataDir);
        Files.writeString(Path.of(TData.tDataDirPath.toString(), "李白/夜宿山寺.txt"), "危楼高百尺");

        //backDirPath 作为已经备份好的，sourceDirPath作为要删除的。
        Map<File, File> deleteFiles = DirCompareService.getDeleteDoubleFileMap(historyDataDir, testDataDir, null);
        Assert.assertEquals(4, deleteFiles.size());

        FileUtil.deleteRepeatFiles(deleteFiles);
        //源文件新增未动
        Assert.assertTrue(Files.exists(Path.of(testDataDir.getAbsolutePath(), "李白/夜宿山寺.txt")));
        //源文件已经删除
        Assert.assertFalse(Files.exists(Path.of(testDataDir.getAbsolutePath(), "readme.md")));
        Assert.assertFalse(Files.exists(Path.of(testDataDir.getAbsolutePath(), "Java/java语法.txt")));
        Assert.assertFalse(Files.exists(Path.of(testDataDir.getAbsolutePath(), "Java/Spring框架.txt")));
        Assert.assertFalse(Files.exists(Path.of(testDataDir.getAbsolutePath(), "李白/静夜思.txt")));
        //源文件夹还在
        Assert.assertTrue(Files.exists(Path.of(testDataDir.getAbsolutePath(), "Java")));
        Assert.assertTrue(Files.exists(Path.of(testDataDir.getAbsolutePath(), "李白")));
        //历史没动
        Assert.assertTrue(Files.exists(Path.of(historyDataDir.getAbsolutePath(), "资料/readme.md")));
        Assert.assertTrue(Files.exists(Path.of(historyDataDir.getAbsolutePath(), "资料/Java/java语法.txt")));
        Assert.assertTrue(Files.exists(Path.of(historyDataDir.getAbsolutePath(), "资料/Java/Spring框架.txt")));
        Assert.assertTrue(Files.exists(Path.of(historyDataDir.getAbsolutePath(), "资料/李白/静夜思.txt")));

        //删除空文件夹
        FileUtil.deleteEmptyDirs(testDataDir.toPath());
        //源文件新增未动
        Assert.assertTrue(Files.exists(Path.of(testDataDir.getAbsolutePath(), "李白/夜宿山寺.txt")));
        //空文件夹已经删除
        Assert.assertFalse(Files.exists(Path.of(testDataDir.getAbsolutePath(), "Java")));

    }


    @After
    public void tearDown() throws Exception {
        FileUtils.forceDelete(TData.tPath.toFile());
    }

}
