package fnote.article.test.integration;

import fnote.domain.config.FlyConfig;
import org.junit.Before;
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
public class TestFlyConfig extends TestArticleIntegration {
    @Resource
    private FlyConfig flyConfig;

    @Before
    public void setup() throws Exception {
        super.setup();
    }

    @Test
    public void lastEdit() throws Exception {
        System.out.println(FlyConfig.onLine);
        assertEquals(2,2);


    }

}
