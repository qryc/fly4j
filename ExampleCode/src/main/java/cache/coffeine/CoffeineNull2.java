package cache.coffeine;



import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;

import java.util.concurrent.TimeUnit;

public class CoffeineNull2 {
    public static void main(String[] args) {
        LoadingCache<String, String> cache = Caffeine.newBuilder()
                .expireAfterWrite(1, TimeUnit.MINUTES)
                .maximumSize(100)
                .build(key->{
                    return null;
                });


        System.out.println(cache.getIfPresent("key1"));


        
    }
}
