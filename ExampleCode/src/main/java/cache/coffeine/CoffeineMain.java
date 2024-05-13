package cache.coffeine;



import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

public class CoffeineMain {
    public static void main(String[] args) {
        Cache<String, String> cache = Caffeine.newBuilder()
                .expireAfterWrite(1, TimeUnit.MINUTES)
                .maximumSize(100)
                .build();

        cache.put("key1", "str1"); //线程安全，考虑多个线程put同一个key的情况

        System.out.println(cache.getIfPresent("key1"));// 缓存中存在相应数据，则返回；不存在返回null

        cache.get("key1",key->"str1");
        
    }
}
