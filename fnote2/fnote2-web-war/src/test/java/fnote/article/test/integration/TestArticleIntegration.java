package fnote.article.test.integration;

import farticle.domain.infrastructure.ArticleRepository;
import fnote.user.domain.infrastructure.UserRepository;
import fnote.user.domain.service.DeployService;
import fnote.user.domain.service.UserService;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;

import javax.annotation.Resource;

/**
 * @author qryc
 */
public abstract class TestArticleIntegration {

    @Resource
    private ArticleRepository articleRepository;
    @Resource
    private UserRepository userRepository;
    @Resource
    private UserService userService;
    @Resource
    private DeployService deployService;
    @Resource
    private StorePathService articlePathService;

    @Before
    public void setup() throws Exception {
//        //删除所有网站数据，包括所有用户和文章
//        FileUtils.deleteQuietly(articlePathService.getRootPtah(FlyConst.PATH_ARTICLE).toFile());
//        //创建管理员以及初始化网站目录
//        deployService.installFlySite("admin@mai.com");
//        //注册一个测试用的用户
//        userService.regUser("liBai", "gliBai@mail");
//        //设置用户的密码
//        userService.updateUserCryptPass("liBai", "pwd123");
//        var flyUserInfo = (UserInfo) userRepository.getUserinfo("liBai");
//        assertEquals("pwd123", flyUserInfo.getUserConfig().getMima());
//        //验证添加前无数据
//        assertEquals(0, articleRepository.findCplArticlesByPin("liBai", WorkSpaceParam.of("")).size());
    }


    @After
    public void tearDown() throws Exception {
        FileUtils.deleteQuietly(articlePathService.getRootDirPath(StorePathService.PATH_ARTICLE).toFile());
    }

}
