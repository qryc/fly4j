package fly4b.article.test;

import farticle.domain.entity.*;


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
public class TestMdArticleParam {

    @Before
    public void setup() throws Exception {

    }

    @Test
    public void convert2ArticleParam() throws Exception {
        var mdArticleParam = new MdArticleParam()
                .setMdContent("a\nb\nc\n")
                .setNoteFileStr("1");
        var articleContent = mdArticleParam.convert2ArticleContent();
        Assert.assertEquals("a", articleContent.title());
        Assert.assertEquals("b\nc\n", articleContent.content());

        mdArticleParam.setMdContent("a\r\nb\r\nc\n");
        articleContent = mdArticleParam.convert2ArticleContent();
        Assert.assertEquals("a", articleContent.title());
        Assert.assertEquals("b\nc\n", articleContent.content());

    }

    @Test
    public void convertByBlog() throws Exception {
        ArticleContent articleContent = new ArticleContent(ArticleAuthEnum.OPEN, "a", "b\nc\n");
        CplArticle cplArticle = new CplArticle(articleContent, new ArticleOrganize());
        MdArticleParam alterBlogParam = MdArticleParam.convert2MdParam(cplArticle);
        Assert.assertEquals("a\nb\nc\n", alterBlogParam.getMdContent());

    }


    @After
    public void tearDown() throws Exception {
    }
}
