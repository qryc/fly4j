package fly4j.common.cache.impl;

import fly4j.common.cache.FlyCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class FlyCacheJVM<T> implements FlyCache<T> {
    static final Logger log = LoggerFactory.getLogger(FlyCacheJVM.class);
    private Map<String, CacheInfo<T>> cacheInfoMap = new ConcurrentHashMap<>();
    private ConcurrentLinkedQueue<String> keyQueue = new ConcurrentLinkedQueue<>();
    private int QUEUESIZE = 1000;

    public FlyCacheJVM() {
    }

    public FlyCacheJVM(int QUEUESIZE) {
        this.QUEUESIZE = QUEUESIZE;
    }

    @Override
    public Optional<T> get(final String key) {
        if (cacheInfoMap.containsKey(key)) {
            CacheInfo<T> cache = cacheInfoMap.get(key);
            // 保证三项数都有才是正常数据，队列，超时时间，存储数据
            if (cache != null && keyQueue.contains(key)
                    && cache.isLive()) {
                return Optional.of(cache.value);
            }
        }
        return Optional.empty();
    }

    @Override
    public long ttl(String key) {
        try {
            if (cacheInfoMap.containsKey(key)) {
                CacheInfo cache = cacheInfoMap.get(key);
                // 保证三项数都有才是正常数据，队列，超时时间，存储数据
                if (cache != null && keyQueue.contains(key)) {
                    return cache.ttl();
                }
            }
        } catch (Exception e) {
            log.error("get获取本地缓存用户信息错误", e);
        }

        return -1;
    }

    @Override
    public void invalidateAll() {
        cacheInfoMap = new ConcurrentHashMap<String, CacheInfo<T>>();
        keyQueue = new ConcurrentLinkedQueue<String>();
    }

    @Override
    public void invalidate(String key) {
        cacheInfoMap.remove(key);
        keyQueue.remove(key);
    }

    @Override
    public Map<String, T> asMap() {
        Map<String, T> map = new HashMap<>();
        cacheInfoMap.forEach((key, value) -> {
            get(key).ifPresent(v -> map.put(key, v));
        });
        return map;
    }

    @Override
    public void put(final String key, final Object obj, long cacheLife) {
        try {
            CacheInfo cacheInfo = cacheInfoMap.get(key);
            if (cacheInfo != null && keyQueue.contains(key)
                    && cacheInfo.isLive()) {
                cacheInfoMap.put(key, new CacheInfo(obj, cacheLife));
            } else {
                int remainNum = QUEUESIZE - keyQueue.size();
                while (remainNum <= 0) {
                    String removeKey = keyQueue.poll();
                    cacheInfoMap.remove(removeKey);
                    remainNum++;
                }
                cacheInfoMap.put(
                        key,
                        new CacheInfo(obj, cacheLife));
            }
            // 保证队列中值唯一
            synchronized (keyQueue) {
                keyQueue.remove(key);
                keyQueue.add(key);
            }
        } catch (Exception e) {
            log.error("put缓存本地缓存用户信息错误", e);
        } finally {
        }
    }

    private static class CacheInfo<T> {
        public CacheInfo(T value, long life) {
            this.value = value;
            this.createTime = System.currentTimeMillis();
            this.life = life;
        }

        public boolean isLive() {
            long now = System.currentTimeMillis();
            return now < getDeadTime();

        }

        private long getDeadTime() {
            return this.createTime + life;
        }

        private long ttl() {
            return getDeadTime() - System.currentTimeMillis();
        }


        private T value;
        private long createTime;
        private long life;
    }
}
