package fnote.persistence.test;

import org.junit.Before;

/**
 * Created by qryc on 2019/6/8.
 */
public abstract class BaseFileKvTest {

    public void resetRootDir2Test() throws Exception {
//        FileUtils.deleteQuietly(FlyConfig.getHomePath().toFile());
    }

    @Before
    public void setup() throws Exception {
        this.resetRootDir2Test();
    }

}

//public abstract class BaseFileKvTest {
//    public static void resetRootDir2Test() throws Exception {
//        NetDiskConfig.rootDirName = "flyTest";
//        FileKVFactory.resetBaseDir4Test();
//        FileUtils.deleteQuietly(new File(NetDiskConfig.getNetDiskRootPath()));
//    }
//
//    @Before
//    public void setup() throws Exception {
//        BaseFileKvTest.resetRootDir2Test();
//    }
//
//}
