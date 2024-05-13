package cache.guava;


import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.concurrent.TimeUnit;

/**
 *
 */
public class Test3_GuavaLoadThreads {


    //创建一个1000长度的缓存，1秒失效
    private static LoadingCache<Long, Stu> cacheLoad = CacheBuilder.newBuilder().maximumSize(1000).expireAfterWrite(30, TimeUnit.MILLISECONDS)
            .build(new CacheLoader<Long, Stu>() {
                @Override
                public Stu load(Long id) throws Exception {
                    //模拟回源加载数据需要时间
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("回源加载：" + id);
                    return new Stu(id, "name" + id);
                }
            });

    public static void main(String[] args) throws Exception {
        //三个线程并发请求同一个数据，只会加载1次
        for (int i = 0; i < 3; i++) {
            new Thread(() -> {
                try {
                    System.out.println(cacheLoad.get(1L));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    private static record Stu(Long id, String name) {
    }


}
