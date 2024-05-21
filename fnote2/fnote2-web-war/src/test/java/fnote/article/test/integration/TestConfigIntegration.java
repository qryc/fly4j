package fnote.article.test.integration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;

/**
 * @author qryc
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-config-integration.xml"})
@ActiveProfiles("test")
public class TestConfigIntegration extends TestArticleIntegration {

    @Test
    public void config() throws Exception {
//        if (OsUtil.isWindows()) {
//            Assert.assertEquals("d:\\flyDataTest\\qryc\\zipData", FlyConfig.getUserZipDataPath("qryc").toString());
//            Assert.assertEquals("d:\\flyDataTest\\qryc\\backData", FlyConfig.getBackDataDirPath("qryc").toString());
//
//            Assert.assertEquals("d:\\flyDataTest\\qryc\\backData\\articles\\blog_1.gwf", NoteStorePathEnum.ARTICLE.getStoreFilePath(IdPin.of(1L,"qryc")).toString());
//        } else if (OsUtil.isMac()) {
//            //路径相关配置
//            assertEquals(System.getProperty("user.home")+"/flyDataTest", FlyConfig.getHomePath().toString());
//            Assert.assertEquals(System.getProperty("user.home")+"/flyDataTest/qryc/backData", FlyConfig.getBackDataDirPath("qryc").toString());
//            Assert.assertEquals(System.getProperty("user.home")+"/flyDataTest/qryc/zipData", FlyConfig.getUserZipDataPath("qryc").toString());
//        }

        //部署相关配置
//        assertEquals(0, deployConfigRepository.getDeployConfig().getFileuploadMaxSize());
        //邮箱配置
//        assertEquals(0, allConfigAdapter.getEmailConfigModel().getFromEmail());

//		Assert.assertEquals("C:\\Program Files (x86)\\MySQL\\MySQL Server 5.5\\bin\\", NetDiskConfig.getMysqlPathWindow());

    }
}
