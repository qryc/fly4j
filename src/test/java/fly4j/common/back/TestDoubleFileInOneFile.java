package fly4j.common.back;

import fly4j.common.back.check.DoubleFileInOneFile;
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

/**
 * @author qryc
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class TestDoubleFileInOneFile {


    @Before
    public void setup() throws Exception {
        TestData.createTestFiles();
    }


    @Test
    public void deleteOneRepeatFile() throws Exception {
        File fileA = Path.of(TestData.sourceDirPath.toString(), "a.txt").toFile();
        File fileACopy = Path.of(TestData.sourceDirPath.toString(), "aCopy.txt").toFile();
        FileUtils.copyFile(fileA,fileACopy);

        LinkedHashMap<String, List<File>> resultMap= DoubleFileInOneFile.doubleFileCheck(TestData.sourceDirPath.toString());
        Assert.assertEquals(1,resultMap.size());
        List<File> files=resultMap.values().iterator().next();
        Assert.assertEquals(2,files.size());
        if(files.get(0).getAbsolutePath().equals(fileA.getAbsolutePath())){
            Assert.assertEquals(fileA.getAbsolutePath(),files.get(0).getAbsolutePath());
            Assert.assertEquals(fileACopy.getAbsolutePath(),files.get(1).getAbsolutePath());
        }else {
            Assert.assertEquals(fileA.getAbsolutePath(),files.get(1).getAbsolutePath());
            Assert.assertEquals(fileACopy.getAbsolutePath(),files.get(0).getAbsolutePath());
        }
    }


    @After
    public void tearDown() throws Exception {
        FileUtils.forceDelete(TestData.testBasePath.toFile());
    }

}
