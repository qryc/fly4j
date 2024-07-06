package fly4b.user.test;

import fnote.user.domain.service.UserSessionService;
import fnote.user.domain.service.UserSessionServiceImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author qryc
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-config-userSession.xml"})
public class TestUserSessionService {
    @Resource
    private UserSessionService userSessionService;


    @Before
    public void setup() throws Exception {
        userSessionService.clear();
        ((UserSessionServiceImpl) userSessionService).setUseSession(true);
    }


    @Test
    public void userSession() throws Exception {
        assertEquals(0,userSessionService.getUserSessions().size());
        var loginUser = userSessionService.addUserSessionRSid("pin1");
        assertTrue(userSessionService.verifyLoginBySession(loginUser));
        assertEquals(1,userSessionService.getUserSessions().size());
    }


    @After
    public void tearDown() throws Exception {
    }

}
