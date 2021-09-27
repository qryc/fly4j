package fly4j.common.crypto;

import fly4j.common.test.util.TData;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Path;

public class TestXorUtil {
    public static void main(String[] args) throws Exception {
        XorUtil.xorFile2File(Path.of(TData.tPath.toString(), "xorSource.txt"), Path.of(TData.tPath.toString(), "xorTarget.fbk"), 123);
        XorUtil.xorFile2File(Path.of(TData.tPath.toString(), "xorTarget.fbk"), Path.of(TData.tPath.toString(), "xorResote.txt"), 123);
    }

    @Before
    public void setup() throws Exception {
        TData.createTestFiles();
    }

    @Test
    public void crypt() throws Exception {
        XorUtil.xorFile2File(Path.of(TData.tDataDirPath.toString(), "readme.md"), Path.of(TData.tDataDirPath.toString(), "readmeCrypt.md"), 123);
        XorUtil.xorFile2File(Path.of(TData.tDataDirPath.toString(), "readmeCrypt.md"), Path.of(TData.tDataDirPath.toString(), "readmeDecrypt.md"), 123);
        Assert.assertEquals("个人资料保存目录", Files.readString(Path.of(TData.tDataDirPath.toString(), "readmeDecrypt.md")));
    }

    @After
    public void tearDown() throws Exception {
        FileUtils.forceDelete(TData.tPath.toFile());
    }

}
