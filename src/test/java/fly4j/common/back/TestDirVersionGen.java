package fly4j.common.back;

import fly4j.common.back.doublefile.DoubleFileInOneFile;
import fly4j.common.back.version.DirVersionGen;
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
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author qryc
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class TestDirVersionGen {


    @Before
    public void setup() throws Exception {
        TData.createTestFiles();
    }


    @Test
    public void deleteOneRepeatFile() throws Exception {
        DirVersionGen.saveDirVersionModel2File(TData.tDataDirPath.toString(), null, TData.tPath.resolve("version.md5"));
        System.out.println(Files.readString(TData.tPath.resolve("version.md5")));
    }


    @After
    public void tearDown() throws Exception {
        FileUtils.forceDelete(TData.tPath.toFile());
    }

}
