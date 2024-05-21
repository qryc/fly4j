package fnote.article.test.integration;

import flynote.article.app.articleview.ArticleViewQueryImpl;
import fnote.article.maintain.ArticleMaintain;
import flynote.article.query.ArticleQuery;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.assertEquals;

/**
 * @author qryc
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-config-integration.xml"})
@ActiveProfiles("test")
public class TestArticleIntegrationQuery extends TestArticleIntegration {
    @Resource
    private ArticleMaintain articleMaintain;
    @Resource
    private ArticleQuery articleQuery;
    @Resource
    private ArticleViewQueryImpl articleViewQuery;
    @Test
    public void t() throws Exception {

    }

//    @Before
//    public void setup() throws Exception {
//        super.setup();
////        ArticleNoteRepositoryFile.cplArticlesCache.invalidateAll();
//        // 添加操作
//        var articleParam = new RichArticleParam().setPin("liBai")
//                .setTitle("望庐山瀑布")
//                .setContent("日照香炉生紫烟，遥看瀑布挂前川。飞流直下三千尺，疑是银河落九天。");
//        articleMaintain.insertRichArticle(articleParam);
//        Thread.sleep(1000);
//        articleParam = new RichArticleParam().setPin("liBai")
//                .setTitle("静夜思")
//                .setContent("床前明月光，疑是地上霜。举头望明月，低头思故乡。");
//        articleMaintain.insertRichArticle(articleParam);
//
//        MdArticleParam mdArticleParam = new MdArticleParam().setPin("liBai")
//                .setMdContent("日记\n今天是个伟大的日子");
//        CplArticle cplArticle = articleMaintain.insertMdArticle(mdArticleParam);
//        ArticleOrganizeParam organizeParam = BeanConvert.organize2Param(cplArticle.getArticleOrganize());
//        organizeParam.setExtStringValue(FlyConst.ORGANIZE_INSURANCE, true);
//        articleMaintain.updateArticleOrganize(IdPin.of(cplArticle.getPin(), cplArticle.getNoteFileStr()), organizeParam, WorkSpaceParam.PERSON);
//    }
//
//    @Test
//    public void lastEdit() throws Exception {
//        var queryParam = new ArticleQueryParam("liBai", "lastEdit")
//                .setExtStringValue(ArticleQueryParam.insurance, true)
//                .setShowMaturity(null).setEncryptPwd("pwd123").setExtStringValue("workspace", FlyConst.WORKSPACE_PERSON);
//        var articleViews = articleQuery.queryArticleInfoVo4Lists(queryParam, null, WorkSpaceParam.PERSON);
//        assertEquals(2, articleViews.size());
//        var articleView = articleViews.get(0);
////        assertEquals("静夜思", articleView.getTitle());
//        String hrefTile = """
//                <font color='red'>
//                    <a style='color:red' href=/article/viewArticle.do?noteFileStr=2>
//                    静夜思
//                    </a>
//                </font>私
//                """;
//
////        assertEquals(hrefTile, articleView.getHrefTitle());
//
//        //关闭保险保护
//        var clientConfig = new ClientConfig();
//        clientConfig.setExtStringValue("insurance", false);
//        queryParam.setExtStringValue(ArticleQueryParam.insurance, false);
//        articleViews = articleQuery.queryArticleInfoVo4Lists(queryParam, null, WorkSpaceParam.PERSON);
//        assertEquals(2, articleViews.size());
//
//
//    }
//
//    @Test
//    public void globalSearchByTitle() throws Exception {
//        var queryParam = new ArticleQueryParam("liBai")
//                .setEncryptPwd("pwd123")
//                .setExtStringValue(ArticleQueryParam.insurance, true)
//                .setBusinessIdentity(FlyConst.ACConst.BAQ_GLOBAL_SEARCH)
//                .setSearchTitle("静夜")
//                .setExtStringValue("workspace",FlyConst.WORKSPACE_PERSON);
//        var articleViews = articleQuery.queryArticleInfoVo4Lists(queryParam, null, WorkSpaceParam.PERSON);
//        assertEquals(1, articleViews.size());
//        var articleView = articleViews.get(0);
////        assertEquals("静夜思", articleView.getTitle());
//
//        queryParam = new ArticleQueryParam("liBai")
//                .setEncryptPwd("pwd123")
//                .setExtStringValue(ArticleQueryParam.insurance, true)
//                .setBusinessIdentity(FlyConst.ACConst.BAQ_GLOBAL_SEARCH)
//                .setSearchTitle("静夜a")
//        ;
//        articleViews = articleQuery.queryArticleInfoVo4Lists(queryParam, null, WorkSpaceParam.PERSON);
//        assertEquals(0, articleViews.size());
//    }
//
//    @Test
//    public void globalSearchByContent() throws Exception {
//        var queryParam = new ArticleQueryParam("liBai")
//                .setExtStringValue(ArticleQueryParam.insurance, true)
//                .setEncryptPwd("pwd123")
//                .setBusinessIdentity(FlyConst.ACConst.BAQ_GLOBAL_SEARCH)
//                .setSearchTitle("明月").setExtStringValue("workspace", FlyConst.WORKSPACE_PERSON);
//        var articleViews = articleQuery.queryArticleInfoVo4Lists(queryParam, null, WorkSpaceParam.PERSON);
//        assertEquals(1, articleViews.size());
//        var articleView = articleViews.get(0);
////        assertEquals("静夜思", articleView.getTitle());
//
//        queryParam = new ArticleQueryParam("liBai")
//                .setExtStringValue(ArticleQueryParam.insurance, true)
//                .setEncryptPwd("pwd123")
//                .setBusinessIdentity(FlyConst.ACConst.BAQ_GLOBAL_SEARCH)
//                .setSearchTitle("明月a")
//        ;
//        articleViews = articleQuery.queryArticleInfoVo4Lists(queryParam, null, WorkSpaceParam.PERSON);
//        assertEquals(0, articleViews.size());
//    }
}
