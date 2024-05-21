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
public class TestLoginFilter {
    private final UserDataBackService userDataBackService = new UserDataBackServiceImpl();

    @Before
    public void setup() throws Exception {

    }

    @Test
    public void getDayFromZipFileName() throws Exception {
        //From: /article/FlyPic/mac-627/image1.png
        //to:   /viewFile?relativePath=backData/pic/mac-627/image1.png
        String flag = "/article/FlyPic/";

//        Assert.assertEquals("/viewFile?relativePath=backData/pic/mac-627/image1.png",
//                NoteLoginFilter.getFlyPicPathStr("/article/FlyPic/mac-627/image1.png", flag));
    }

    @After
    public void tearDown() throws Exception {
    }
}
