package fly4b.user.test;

import farticle.domain.entity.MdArticleParam;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;

/**
 * @author qryc
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class TestArticleMaintain {
    @Before
    public void setup() throws Exception {
    }


    @Test
    public void insertMdArticle() throws Exception {


//        //验证参数转换,标题a，内容b，通过换行分割
//        var mdArticleParam = new MdArticleParam()
//                .setMdContent("a\nb")
//                .setPin("qryc")
//                .setEncryptPwd("test");
//        var articleParam = mdArticleParam.convert2ArticleParam();
//        assertEquals("a",articleParam.getArticleContent().title());
//        assertEquals("b",articleParam.getArticleContent().content());
//        assertEquals("qryc",articleParam.getPin());
//        assertEquals("test",articleParam.getEncryptPwd());
//
//        //验证插入
//        var articleMaintain = new ArticleMaintainImpl();
//        var articleRepository = Mockito.mock(ArticleRepository.class);
//        articleMaintain.setArticleRepository(articleRepository);
//        var executor = Mockito.mock(ThreadPoolTaskExecutor.class);
//        articleMaintain.setFlyExecutor(executor);
//
//
//        articleMaintain.insertMdArticle(articleParam);
//
//        verify(articleRepository, times(1)).insertCplArticle(argThat(cplArticle -> {
//            assertEquals("a", cplArticle.getArticleContent().title());
//            assertEquals("991BCB8C0633F4A73C2BD99825C70C09", cplArticle.getArticleContent().content());
//            return true;
//        }));
//        verify(executor, Mockito.times(1)).execute(Mockito.any(Runnable.class));
    }


    @After
    public void tearDown() throws Exception {
    }

}
