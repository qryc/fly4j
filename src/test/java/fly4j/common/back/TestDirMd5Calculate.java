package fly4j.common.back;


import fly4j.common.back.version.BackModel;
import fly4j.common.back.version.DirDigestCalculate;
import fly4j.common.file.FileAndDirFilter;
import fly4j.common.lang.JsonUtils;
import fly4j.common.os.OsUtil;
import fly4j.test.util.TData;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static fly4j.test.util.TData.tDataDirPath;

/**
 * @author qryc
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class TestDirMd5Calculate {
    static final Logger log = LoggerFactory.getLogger(TestDirMd5Calculate.class);

    public TestDirMd5Calculate() {
        FileAndDirFilter fileAndDirFilter = new FileAndDirFilter();
//        fileAndDirFilter.setFilterDirNames(Set.of(""));
    }

    @Before
    public void setup() throws Exception {
        TData.createTestFiles();
    }

    @Test
    public void genDirMd5VersionTagLen() throws Exception {
        if (OsUtil.isWindows()) {
            return;
        }
        String md5 = """
                {"":"dir","Java":"dir","Java/java语法.txt":"25","Java/Spring框架.txt":"33","todo":"dir","readme.md":"24","李白":"dir","李白/静夜思.txt":"15"}""";
        BackModel.DirVersionCheckParam dirMd5Param = new BackModel.DirVersionCheckParam(BackModel.DigestType.LEN, true, null);
        String md5FileStr = JsonUtils.writeValueAsString(DirDigestCalculate.getDirDigestMap(tDataDirPath.toString(), dirMd5Param));
        Assert.assertEquals(md5, md5FileStr);
    }

    @Test
    public void genDirMd5VersionTagMd5() throws Exception {
        if (OsUtil.isWindows()) {
            return;
        }
        String md5 = """
                {"":"dir","Java":"dir","Java/java语法.txt":"f29a628831fe901bbdd3132bbdc2313e","Java/Spring框架.txt":"5fb14d685c15f75c92d243da709cc28b","todo":"dir","readme.md":"a723b646fe33ab2f0c8e8ed4c4583f0b","李白":"dir","李白/静夜思.txt":"7bae0c4e8ec3f7dededc3450137efa04"}""";
        BackModel.DirVersionCheckParam dirMd5Param = new BackModel.DirVersionCheckParam(BackModel.DigestType.MD5, true, null);
        String md5FileStr = JsonUtils.writeValueAsString(DirDigestCalculate.getDirDigestMap(tDataDirPath.toString(), dirMd5Param));
        Assert.assertEquals(md5, md5FileStr);
    }

    @After
    public void tearDown() throws Exception {
        FileUtils.forceDeleteOnExit(TData.tPath.toFile());
    }


}
