package fly4j.common.crypto;

import fly4j.test.util.TestData;

import java.nio.file.Path;

public class TestXorUtil {
    public static void main(String[] args) throws Exception {
        XorUtil.encryptFile(Path.of(TestData.tSourceDir, "xorSource.txt"), Path.of(TestData.tTargetDir, "xorTarget.fbk"), 123);
        XorUtil.decryptFile(Path.of(TestData.tTargetDir, "xorTarget.fbk"), Path.of(TestData.tTargetDir, "xorResote.txt"), 123);
    }

}
