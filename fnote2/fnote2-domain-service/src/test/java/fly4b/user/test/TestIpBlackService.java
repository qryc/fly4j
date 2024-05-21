package fly4b.user.test;

import fnote.user.domain.service.BlackIpService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 * @author qryc
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-config-ipblack.xml"})
public class TestIpBlackService {
    @Resource
    private BlackIpService blackIpService;


    @Before
    public void setup() throws Exception {
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

    public void setBlackIpService(BlackIpService blackIpService) {
        this.blackIpService = blackIpService;
    }
}
