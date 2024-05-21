package fnote.user.domain.service;

import fly4j.common.cache.FlyCache;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by qryc on 2015/9/4.
 */
public class BlackIpServiceImpl implements BlackIpService {
    private static final Log log = LogFactory.getLog(BlackIpServiceImpl.class);
    private FlyCache<AtomicLong> blackIpsCache;
    private final long blackLifeTime = 1000 * 60 * 5;

    @Override
    public synchronized void addBlackIP(String ip) {
        AtomicLong count = blackIpsCache.get(ip);
        if (null == count) {
            blackIpsCache.put(ip, new AtomicLong(1), blackLifeTime);
            return;
        }
        count.addAndGet(1);

    }

    @Override
    public synchronized void clearBlackIP() {
        blackIpsCache.invalidateAll();
    }

    @Override
    public boolean isBlack(String ip) {
        AtomicLong count = blackIpsCache.get(ip);
        //ip判断
        return null != count && count.get() > 2;
    }

    @Override
    public Map<String, AtomicLong> getAllLoginFailIps() {
        Map<String, AtomicLong> map = new HashMap<>();
        blackIpsCache.asMap().forEach((key, value) -> {
            map.put(key, value);
        });
        return map;
    }

    public void setBlackIpsCache(FlyCache blackIpsCache) {
        this.blackIpsCache = blackIpsCache;
    }
}
