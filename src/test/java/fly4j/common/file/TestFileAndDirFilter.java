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
        FileAndDirFilter fileAndDirFilter;
        fileAndDirFilter = new FileAndDirFilter();
        fileAndDirFilter.setFilterDirNames(Set.of(".idea", "target", ".DS_Store", ".git"));
        fileAndDirFilter.setFilterSuffixNames(Set.of("iml", "md5"));
        Assert.assertTrue(fileAndDirFilter.accept(new File("/export/.idea")));
        Assert.assertTrue(fileAndDirFilter.accept(new File("/export/.idea/")));
        Assert.assertFalse(fileAndDirFilter.accept(new File("/export/.ideaA")));
        Assert.assertFalse(fileAndDirFilter.accept(new File("/export/.idea/a.txt")));
    }

    @After
    public void tearDown() throws Exception {
        FileUtils.forceDelete(TData.tPath.toFile());
    }

}
