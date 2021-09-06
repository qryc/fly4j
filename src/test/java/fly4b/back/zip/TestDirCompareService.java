package fly4b.back.zip;


import fly4j.common.back.*;
import fly4j.common.back.model.VersionType;
import fly4j.common.back.param.DirVersionCheckParam;
import fly4j.common.file.DirMd5Calculate;
import fly4j.common.file.FileAndDirFilter;
import fly4j.common.lang.JsonUtils;
import fly4j.common.os.OsUtil;
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

import static fly4j.test.util.TestData.sourceDirPath;

/**
 * @author qryc
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class TestDirCompareService {
    static final Logger log = LoggerFactory.getLogger(TestDirCompareService.class);
    private DirCompareService dirCompareService;

    public TestDirCompareService() {
        FileAndDirFilter fileAndDirFilter = new FileAndDirFilter();
//        fileAndDirFilter.setFilterDirNames(Set.of(""));
        DirCompareService dirCompareServiceImpl = new DirCompareService();
        dirCompareService = dirCompareServiceImpl;
    }

    @Before
    public void setup() throws Exception {
        FileUtils.deleteQuietly(TestData.testBasePath.toFile());
        TestData.createTestFiles();
    }

    @Test
    public void genDirMd5VersionTagLen() throws Exception {
        if (OsUtil.isWindows()) {
            return;
        }
        String md5 = """
                {"":"dir","childDir":"dir","childDir/aa.txt":"8","childDir/bb.txt":"8","c.txt":"7","b.txt":"7","a.txt":"7"}""";
        DirVersionCheckParam dirMd5Param = new DirVersionCheckParam(VersionType.LEN, true, null);
        String md5FileStr = JsonUtils.writeValueAsString(DirMd5Calculate.getDirMd5Map(sourceDirPath.toString(), dirMd5Param));
        Assert.assertEquals(md5, md5FileStr);
    }

    @Test
    public void genDirMd5VersionTagMd5() throws Exception {
        if (OsUtil.isWindows()) {
            return;
        }
        String md5 = """
                {"":"dir","childDir":"dir","childDir/aa.txt":"ce4f75647b15fc7fa4f01ad9f856d307","childDir/bb.txt":"35cb9fa3d1b1d570a7a64c7d27b4ac27","c.txt":"29fbb78de8005a02cc22a2550c383745","b.txt":"f0a408d9c5b8e4b888385a6c630beba4","a.txt":"c173b145b212ca55558eba13aac59aa3"}""";
        DirVersionCheckParam dirMd5Param = new DirVersionCheckParam( VersionType.MD5, true, null);
        String md5FileStr = JsonUtils.writeValueAsString(DirMd5Calculate.getDirMd5Map(sourceDirPath.toString(),dirMd5Param));
        Assert.assertEquals(md5, md5FileStr);
    }

    @After
    public void tearDown() throws Exception {
//        FileUtils.forceDeleteOnExit(TestData.testPath.toFile());
    }


}
