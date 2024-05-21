package fly4u.core.service;

import fly4j.common.cache.impl.FlyCacheJVM;
import fnote.user.domain.service.BlackIpServiceImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * @author qryc
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class TestIpBlackService {
    private final BlackIpServiceImpl blackIpService = new BlackIpServiceImpl();


    @Before
    public void setup() throws Exception {
        blackIpService.setBlackIpsCache(new FlyCacheJVM());
        blackIpService.clearBlackIP();
    }

    @Test
    public void memory() throws Exception {
        blackIpService.clearBlackIP();
        // 测试初始值
        assertEquals(0, blackIpService.getAllLoginFailIps().size());
        //测试内存控制
        for (int i = 1; i <= 10000; i++) {
            blackIpService.addBlackIP("127.0.0." + i);
        }
        int size = blackIpService.getAllLoginFailIps().size();
        System.out.println(size);
        assertTrue(size > 900 && size <= 1000);
    }

    @Test
    public void blackIp() throws Exception {
        assertFalse(blackIpService.isBlack("127.0.0.1"));
        blackIpService.addBlackIP("127.0.0.1");
        assertFalse(blackIpService.isBlack("127.0.0.1"));
        blackIpService.addBlackIP("127.0.0.1");
        assertFalse(blackIpService.isBlack("127.0.0.1"));
        blackIpService.addBlackIP("127.0.0.1");
        assertTrue(blackIpService.isBlack("127.0.0.1"));
    }


    @After
    public void tearDown() throws Exception {
    }

}
