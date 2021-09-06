package fly4j.common.lang;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * @author qryc
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class TestFlyString {

    @Before
    public void setup() throws Exception {

    }

    @Test
    public void compare2Str() throws Exception {
        String str1 = "/Users/testu/OneDrive/光盘制作/照片视频光盘/2021/05/20210523_071804458_iOS.jpg";
        String str2 = "/Users/testu/OneDrive/光盘制作/照片视频光盘/家庭旅游/202105济南/20210523_071804458_iOS.jpg";
        Map<String,String> result= FlyString.compare2Str(List.of(str1,str2));
        assertEquals("/Users/testu/OneDrive/光盘制作/照片视频光盘/", result.get("same"));
        assertEquals("2021/05/20210523_071804458_iOS.jpg", result.get("0"));
        assertEquals("家庭旅游/202105济南/20210523_071804458_iOS.jpg", result.get("1"));
    }

    @After
    public void tearDown() throws Exception {
    }
}
