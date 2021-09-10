package fly4j.common.back;

import fly4j.test.util.TestData;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import java.io.File;
import java.nio.file.Path;

/**
 * Created by qryc on 2021/9/10
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class TestFilePath {
    @Test
    public void deleteOneRepeatFile() throws Exception {
        File fileA = Path.of(TestData.sourceDirPath.toString(), "a.txt").toFile();
        File fileB = new File(Path.of(TestData.sourceDirPath.toString(), "a.txt").toString());
        Assert.assertEquals(fileA, fileB);
        Path pathA = Path.of(TestData.sourceDirPath.toString(), "/a.txt");
        Path pathB = Path.of(TestData.sourceDirPath.toString(), "a.txt");
        Assert.assertEquals(pathA, pathB);
    }
}
