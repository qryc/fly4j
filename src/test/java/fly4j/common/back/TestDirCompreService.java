package fly4j.common.back;

import fly4j.common.back.compare.DirCompareService;
import fly4j.common.back.doublefile.DoubleFileInOneFile;
import fly4j.test.util.TestData;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import java.io.File;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author qryc
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class TestDirCompreService {


    @Before
    public void setup() throws Exception {
        TestData.createTestFiles();
    }


    @Test
    public void deleteOneRepeatFile() throws Exception {
        File fileA = Path.of(TestData.sourceDirPath.toString(), "a.txt").toFile();
        File fileB = new File(Path.of(TestData.sourceDirPath.toString(), "a.txt").toString());
        Assert.assertEquals(fileA, fileB);
        File sourceWithLeft = new File(TestData.sourceDirPath.toString() + "/");
        Path targetPath = Path.of(TestData.backDirPath.toString(), "sourcePath");

        Map<File, File> deleteFiles = DirCompareService.getDeleteDoubleFileMap(TestData.backDirPath.toFile(), TestData.sourceDirPath.toFile(), null);
        Assert.assertEquals(0, deleteFiles.size());
        FileUtils.copyDirectoryToDirectory(sourceWithLeft, TestData.backDirPath.toFile());
        //backDirPath 作为已经备份好的，sourceDirPath作为要删除的。
         deleteFiles = DirCompareService.getDeleteDoubleFileMap(TestData.backDirPath.toFile(), TestData.sourceDirPath.toFile(), null);
        Assert.assertEquals(5, deleteFiles.size());
        Assert.assertEquals(Path.of(targetPath.toString(), "a.txt").toFile(), deleteFiles.get(Path.of(TestData.sourceDirPath.toString(), "a.txt").toFile()));
        System.out.println(deleteFiles);

    }


    @After
    public void tearDown() throws Exception {
        FileUtils.forceDelete(TestData.testBasePath.toFile());
    }

}