package fly4j.common.file;

import fly4j.test.util.TData;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

/**
 * @author qryc
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class TestFileAndDirFilter {


    @Before
    public void setup() throws Exception {
        TData.createTestFiles();
    }


    @Test
    public void testFileSub() throws Exception {
        Files.createDirectories(TData.tPath.resolve(".idea"));
        Files.writeString(TData.tPath.resolve(".idea/a.txt"), "xxxxxx");
        FileAndDirFilter fileAndDirFilter = new FileAndDirFilter(Set.of(".idea", "target", ".DS_Store", ".git"),
                Set.of("iml", "md5"));
        Assert.assertTrue(fileAndDirFilter.accept(new File(TData.tPath.toString() + "/.idea")));
        Assert.assertTrue(fileAndDirFilter.accept(new File(TData.tPath.toString() + "/.idea/")));
        Assert.assertFalse(fileAndDirFilter.accept(new File(TData.tPath.toString() + "/.ideaA")));
        Assert.assertFalse(fileAndDirFilter.accept(new File(TData.tPath.toString() + "/.idea/a.txt")));
    }

    @After
    public void tearDown() throws Exception {
        FileUtils.forceDelete(TData.tPath.toFile());
    }

}
