package fly4j.common.cache.impl;

import fly4j.common.cache.FlyCache;
import fly4j.common.cache.LimitRate;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class LimitRateImpl implements LimitRate {
    private FlyCache<AtomicInteger> flyCache;
    private boolean isLimit = true;
    private int time = 20;
    private int limitNum = 2;

    public LimitRateImpl(int time, int limitNum) {
        this.time = time;
        this.limitNum = limitNum;
    }

    public LimitRateImpl(FlyCache<AtomicInteger> flyCache, int time, int limitNum) {
        this.flyCache = flyCache;
        this.time = time;
        this.limitNum = limitNum;
    }

    //限流
    @Override
    public boolean isHotLimit(String id) {
        final String key = "limit" + id;
        Optional.ofNullable(flyCache.get(key)).ifPresentOrElse(num -> {
                    int countNum = num.incrementAndGet();
                    if (countNum <= 5) {  //可监控
                    } else if (countNum <= 10) {  //可监控
                    } else if (countNum <= 15) {  //可监控
                    } else if (countNum <= 20) {  //可监控
                    } else {//可监控
                    }
                },
                () -> flyCache.put(key, new AtomicInteger(1), time));
        return isLimit && flyCache.get(key).get() > limitNum;
    }
}
