package fly4b.product.test;

import farticle.domain.view.PcViewDecorator;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

/**
 * @author qryc
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class TestHtmlUtil {

    @Before
    public void setup() throws Exception {

    }

    @Test
    public void optimizMobileView() throws Exception {
//    	String oldStr="<img src=\"https://upload-images.jianshu.io/upload_images/10120036-fe97aa0de27dc252.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240\" alt=\"image.png\" /></p>";
//    	String imgStr="<img src=\"https://upload-images.jianshu.io/upload_images/10120036-fe97aa0de27dc252.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240\" alt=\"image.png\" />";
//		System.out.println(oldStr.replace(imgStr,"sdf"));
        Assert.assertEquals("<img src=\"https://upload-images.jianshu.io/upload_images/10120036-fe97aa0de27dc252.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240\" alt=\"image.png\" width=\"90%\" /></p>", PcViewDecorator.optimizView("<img src=\"https://upload-images.jianshu.io/upload_images/10120036-fe97aa0de27dc252.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240\" alt=\"image.png\" /></p>"));
        Assert.assertEquals("<p><img src=\" /pic/iknowDisk/设计/数据迁徙/数据迁徙-切量.jpg\" width=\"90%\"  />", PcViewDecorator.optimizView("<p><img src=\" /pic/iknowDisk/设计/数据迁徙/数据迁徙-切量.jpg\" width=\"900\"  />"));
        Assert.assertEquals("<img src=\"/pic/iknowDisk/设计/数据迁徙/数据迁徙-切量.jpg\" alt=\"\" width=\"90%\" /></p>", PcViewDecorator.optimizView("<img src=\"/pic/iknowDisk/设计/数据迁徙/数据迁徙-切量.jpg\" alt=\"\" /></p>"));
        Assert.assertEquals("<img src=\"/pic/iknowDisk/设计/数据迁徙/数据迁徙-切量.jpg\" alt=\"\" width=\"90%\" ></p>", PcViewDecorator.optimizView("<img src=\"/pic/iknowDisk/设计/数据迁徙/数据迁徙-切量.jpg\" alt=\"\" ></p>"));
    }


    @After
    public void tearDown() throws Exception {
    }
}
