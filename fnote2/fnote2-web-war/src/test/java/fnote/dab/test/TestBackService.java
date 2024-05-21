package fnote.dab.test;

import flynote.application.ops.back.UserDataBackService;
import flynote.application.ops.back.UserDataBackServiceImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

/**
 * @author qryc
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class TestBackService {
    private final UserDataBackService userDataBackService = new UserDataBackServiceImpl();

    @Before
    public void setup() throws Exception {

    }

    @Test
    public void getDayFromZipFileName() throws Exception {
//        assertEquals("down2015-05-15",
//                backService.getDayFromZipFileName("down2015-05-15.zip"));
//        backConfigService.getBackDirs().forEach(dir-> System.out.println(FilenameUtils.getBaseName(FilenameUtils.getPathNoEndSeparator(dir))));
        System.out.println();
    }

    @After
    public void tearDown() throws Exception {
    }
}
