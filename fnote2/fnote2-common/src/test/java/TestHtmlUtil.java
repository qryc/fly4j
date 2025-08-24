import fnote.common.web.HtmlUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import java.util.List;

@RunWith(BlockJUnit4ClassRunner.class)
public class TestHtmlUtil {
    @Test
    public void getSrcFromImagTag() {
        String html = "<img src='example1.jpg'> <img src=\"example2.jpg\"> <img src=\"example3.jpg\">";
        List<String> srcList = HtmlUtil.getSrcFromImagTag(html);
        Assert.assertEquals("example1.jpg", srcList.get(0));
        Assert.assertEquals("example2.jpg", srcList.get(1));
        Assert.assertEquals("example3.jpg", srcList.get(2));

    }
}
