package fly4u.core.service;

import fly4j.common.cache.impl.FlyCacheJVM;
import fnote.user.domain.service.UserSessionServiceImpl;
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
public class TestUserSessionService {
    private final UserSessionServiceImpl userSessionService = new UserSessionServiceImpl();


    @Before
    public void setup() throws Exception {
        userSessionService.setUserSessionCache_sid(new FlyCacheJVM());
        userSessionService.clear();
        userSessionService.setUseSession(true);
    }


    @Test
    public void userSession() throws Exception {
        assertEquals(0, userSessionService.getUserSessions().size());
        var sid = userSessionService.addUserSessionRSid("pin1");
//        assertTrue(userSessionService.verifyLoginBySession("pin1", sid));
        assertEquals(1, userSessionService.getUserSessions().size());
    }


    @After
    public void tearDown() throws Exception {
    }

}
