package fnote.article.test.integration;

import farticle.domain.entity.*;
import flynote.article.app.articleview.ArticleViewQueryImpl;
import flynote.application.manual.ManualService;
import fnote.article.maintain.ArticleMaintain;
import flynote.article.query.ArticleLoad;
import farticle.domain.infrastructure.ArticleRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author qryc
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-config-integration.xml"})
@ActiveProfiles("test")
public class TestArticleIntegrationMaintain extends TestArticleIntegration {
    @Resource
    private ManualService manual;
    @Resource
    private ArticleMaintain articleMaintain;
    @Resource(name = "articleNoteRepositoryFile")
    private ArticleRepository articleNoteRepositoryFile;
    @Resource
    private ArticleLoad articleLoad;
    @Resource
    private ArticleViewQueryImpl articleViewQuery;


    @Test
    public void manual() throws Exception {
//        assertTrue(manual.getAddArticleManual().contains("正文第一行"));
    }

    /**
     * 批量插入大量文章，辅助测试，博客数量过小，容易验证错误。
     * 例如修改，如果只有一条，不根据ID也是对的
     */
    private void insertArticles(int i) throws Exception {
        MdArticleParam mdArticleParam = new MdArticleParam()
                .setPin("liBai")
                .setMdContent("test%s\ntestContent%s".formatted(i, i));
        articleMaintain.insertMdArticle(mdArticleParam);
    }

    @Test
    public void curdArticle4() throws Exception {

//        //验证添加前无数据
//        assertEquals(0, articleNoteRepositoryFile.findCplArticlesByPin("liBai", WorkSpaceParam.PERSON).size());
//
//        //添加一条文章，标题加*私有
//        MdArticleParam mdArticleParam = new MdArticleParam()
//                .setPin("liBai")
//                .setMdContent("望庐山瀑布\n日照香炉生紫烟，遥看瀑布挂前川。飞流直下三千尺，疑是银河落九天。");
//        String fileStrLuShan = articleMaintain.insertMdArticle(mdArticleParam).getNoteFileStr();
//        //验证文章查找功能
//        ArticleView articleView = articleViewQuery.getArticleView(IdPin.of("liBai", fileStrLuShan), "pwd123", true, WorkSpaceParam.PERSON);
//        assertEquals("日照香炉生紫烟，遥看瀑布挂前川。飞流直下三千尺，疑是银河落九天。", articleView.getCplArticle().getArticleContent().content());
//
//        //修改为不加密
//        ArticleOrganize organize = articleView.getCplArticle().getArticleOrganize();
//        ArticleOrganizeParam organizeParam = BeanConvert.organize2Param(organize);
//        organizeParam.setOpen(ArticleAuthEnum.REAL_OPEN.getAuthCode());
//        articleMaintain.updateArticleOrganize(IdPin.of("liBai", fileStrLuShan), organizeParam, WorkSpaceParam.PERSON);
//        //验证文章查找功能
//        articleView = articleViewQuery.getArticleView(IdPin.of("liBai", fileStrLuShan), "pwd123", true, WorkSpaceParam.PERSON);
//        assertEquals("日照香炉生紫烟，遥看瀑布挂前川。飞流直下三千尺，疑是银河落九天。", articleView.getCplArticle().getArticleContent().content());
//        //验证查找文章列表结果为一条
//        List<CplArticle> cplArticles = articleNoteRepositoryFile.findCplArticlesByPin("liBai", WorkSpaceParam.PERSON);
//        assertEquals(1, cplArticles.size());
//        assertEquals(ArticleOrganize.EDITOR_MD, cplArticles.get(0).getArticleOrganize().getEditor());
//        assertEquals("望庐山瀑布", cplArticles.get(0).getArticleContent().title());
//        assertEquals("日照香炉生紫烟，遥看瀑布挂前川。飞流直下三千尺，疑是银河落九天。", cplArticles.get(0).getArticleContent().content());

    }

    @Test
    public void curdArticle() throws Exception {

//        //验证添加前无数据
//        assertEquals(0, articleNoteRepositoryFile.findCplArticlesByPin("liBai", WorkSpaceParam.PERSON).size());
//
//        //先插入10条
//        for (int i = 0; i < 10; i++) {
//            insertArticles(i);
//        }
//        //添加一条文章，标题加*私有
//        MdArticleParam mdArticleParam = new MdArticleParam()
//                .setPin("liBai")
//                .setMdContent("望庐山瀑布\n日照香炉生紫烟，遥看瀑布挂前川。飞流直下三千尺，疑是银河落九天。");
//        String fileStrLuShan = articleMaintain.insertMdArticle(mdArticleParam).getNoteFileStr();
//        //之后插入20条
//        for (int i = 10; i < 30; i++) {
//            insertArticles(i);
//        }
//
//        //验证查找文章列表结果为一条
//        List<CplArticle> cplArticles = articleNoteRepositoryFile.findCplArticlesByPin("liBai", WorkSpaceParam.PERSON);
//        assertEquals(31, cplArticles.size());
//        assertEquals(ArticleOrganize.EDITOR_MD, cplArticles.get(10).getArticleOrganize().getEditor());
//
//        //验证持久层根据ID查找单个文章，第一个添加的ID为1
//        CplArticle cplArticleEncrypt = articleNoteRepositoryFile.getCplArticleById4Edit(IdPin.of("liBai", fileStrLuShan), WorkSpaceParam.PERSON);
////        String encryptContent = AESUtil.encryptStr2HexStr("日照香炉生紫烟，遥看瀑布挂前川。飞流直下三千尺，疑是银河落九天。", "pwd123");
////        assertEquals(encryptContent, cplArticleEncrypt.getArticleContent().content());
//        //验证文章管理服务的查找功能
//        CplArticle cplArticleDecrypt = articleMaintain.getCplArticleDecrypt(IdPin.of("liBai", fileStrLuShan), "pwd123", WorkSpaceParam.PERSON);
//        assertEquals(ArticleOrganize.EDITOR_MD, cplArticleDecrypt.getArticleOrganize().getEditor());
//        assertEquals("日照香炉生紫烟，遥看瀑布挂前川。飞流直下三千尺，疑是银河落九天。", cplArticleDecrypt.getArticleContent().content());
//
//        //验证文章查找功能
//        ArticleView articleView = articleViewQuery.getArticleView(IdPin.of("liBai", fileStrLuShan), "pwd123", true, WorkSpaceParam.PERSON);
//        assertEquals("日照香炉生紫烟，遥看瀑布挂前川。飞流直下三千尺，疑是银河落九天。", articleView.getCplArticle().getArticleContent().content());
////        assertEquals("<p>日照香炉生紫烟，遥看瀑布挂前川。飞流直下三千尺，疑是银河落九天。</p>" + "\n", articleView.getHtml());
//
//        //cache
//        articleView = articleViewQuery.getArticleView(IdPin.of("liBai", fileStrLuShan), "pwd123", true, WorkSpaceParam.PERSON);
////        assertEquals("日照香炉生紫烟，遥看瀑布挂前川。飞流直下三千尺，疑是银河落九天。", articleView.getContent());
////        assertEquals("<p>日照香炉生紫烟，遥看瀑布挂前川。飞流直下三千尺，疑是银河落九天。</p>" + "\n", articleView.getHtml());
//        //CACHE_DB
//        articleView = articleViewQuery.getArticleView(IdPin.of("liBai", fileStrLuShan), "pwd123", true, WorkSpaceParam.PERSON);
////        assertEquals("日照香炉生紫烟，遥看瀑布挂前川。飞流直下三千尺，疑是银河落九天。", articleView.getContent());
////        assertEquals("<p>日照香炉生紫烟，遥看瀑布挂前川。飞流直下三千尺，疑是银河落九天。</p>" + "\n", articleView.getHtml());
//        assertEquals(31, articleNoteRepositoryFile.findCplArticlesByPin("liBai", WorkSpaceParam.PERSON).size());
//        //修改
//        var mdArticleParam2 = new MdArticleParam()
//                .setNoteFileStr(fileStrLuShan)
//                .setPin("liBai")
//                .setMdContent("静夜思\n疑是地上霜");
//        String fileStrJingYe = articleMaintain.updateMdArticle(mdArticleParam2).getNoteFileStr();
//        assertEquals(31, articleNoteRepositoryFile.findCplArticlesByPin("liBai", WorkSpaceParam.PERSON).size());
//
//        cplArticleEncrypt = articleMaintain.getCplArticleDecrypt(IdPin.of("liBai", fileStrJingYe), "pwd123", WorkSpaceParam.PERSON);
//        assertEquals("疑是地上霜", cplArticleEncrypt.getArticleContent().content());
//        assertEquals("静夜思", cplArticleEncrypt.getArticleContent().title());
//        assertTrue(cplArticleEncrypt.getArticleOrganize().isMdEditor());
//
//        //append
//        articleMaintain.appendFrontOfContent(IdPin.of("liBai", fileStrJingYe), "床前明月光,", "pwd123", WorkSpaceParam.PERSON);
//        assertEquals(31, articleNoteRepositoryFile.findCplArticlesByPin("liBai", WorkSpaceParam.PERSON).size());
//        cplArticleEncrypt = articleMaintain.getCplArticleDecrypt(IdPin.of("liBai", fileStrJingYe), "pwd123", WorkSpaceParam.PERSON);
//        assertEquals(ArticleOrganize.EDITOR_MD, cplArticleEncrypt.getArticleOrganize().getEditor());
//        assertEquals("床前明月光,\n疑是地上霜", cplArticleEncrypt.getArticleContent().content());
//        assertEquals("静夜思", cplArticleEncrypt.getArticleContent().title());
//        articleView = ArticleView.createArticleView(cplArticleEncrypt, false);
////        assertEquals("<p>床前明月光,<br/>\n" +
////                "疑是地上霜</p>\n", articleView.getHtml());
//
//        //组织结构为富文本 orgnize
//        ArticleOrganize organize = cplArticleEncrypt.getArticleOrganize();
//        ArticleOrganizeParam organizeParam = BeanConvert.organize2Param(organize);
//        organizeParam.setEditor(ArticleOrganize.EDITOR_HTML);
//        articleMaintain.updateArticleOrganize(IdPin.of("liBai", fileStrJingYe), organizeParam, WorkSpaceParam.PERSON);
//        cplArticleEncrypt = articleMaintain.getCplArticleDecrypt(IdPin.of("liBai", fileStrJingYe), "pwd123", WorkSpaceParam.PERSON);
//        assertEquals("静夜思", cplArticleEncrypt.getArticleContent().title());
//        assertEquals(ArticleOrganize.EDITOR_HTML, cplArticleEncrypt.getArticleOrganize().getEditor());
//        articleView = ArticleView.createArticleView(cplArticleEncrypt, false);
//        assertEquals("床前明月光,\n疑是地上霜", articleView.getHtml());
    }

}
