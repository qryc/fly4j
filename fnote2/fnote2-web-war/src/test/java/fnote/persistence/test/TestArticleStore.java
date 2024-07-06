package fnote.persistence.test;

import farticle.domain.infrastructure.ArticleRepository;
import fnote.common.StorePathService;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * @author qryc
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-config-integration.xml"})
@ActiveProfiles("test")
public class TestArticleStore {
    @Resource
    private ArticleRepository articleRepositoryFile;
    @Resource
    private StorePathService pathService;

    @Before
    public void setup() throws Exception {
        FileUtils.deleteQuietly(pathService.getRootPtah(StorePathService.PATH_ARTICLE).toFile());
    }

    @Test
    public void insertOrUpdateArticle() throws Exception {
//        // 添加前无数据
//        assertEquals(0, articleRepositoryFile.findCplArticlesByPin("liBai", WorkSpaceParam.PERSON).size());
//
//        // insert first
//        var article = new ArticleContent(ArticleAuthEnum.ENCRYPT_PRI, "java开发", null);
//        var cplArticle = new CplArticle(article, new ArticleOrganize()).setExtId(101L).setPin("liBai");
//        String fileStr = articleRepositoryFile.insertCplArticle(cplArticle);
//        articleRepositoryFile.findCplArticlesByPin("liBai", WorkSpaceParam.PERSON);
//        assertEquals(1, articleRepositoryFile.findCplArticlesByPin("liBai", WorkSpaceParam.PERSON).size());
//        assertEquals(101, articleRepositoryFile.findCplArticlesByPin("liBai", WorkSpaceParam.PERSON).get(0).getExtId().intValue());
//
//
//        //delete
//        articleRepositoryFile.deleteCplArticleById(IdPin.of("liBai", fileStr));
//        assertEquals(0, articleRepositoryFile.findCplArticlesByPin("liBai", WorkSpaceParam.PERSON).size());
//
//        //insert two
//        articleRepositoryFile.insertCplArticle(cplArticle);
//        assertEquals(1, articleRepositoryFile.findCplArticlesByPin("liBai", WorkSpaceParam.PERSON).size());
//        assertEquals(101, articleRepositoryFile.findCplArticlesByPin("liBai", WorkSpaceParam.PERSON).get(0).getExtId().intValue());

    }


    @After
    public void tearDown() throws Exception {
    }
}
