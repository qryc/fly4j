package cache.guava;


import com.google.common.base.Ticker;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 *
 */
public class Test2_GuavaGetThreads {


    //创建一个1000长度的缓存，1秒失效
    private static Cache<Long, Stu> skuCache = CacheBuilder.newBuilder().maximumSize(1000)
            .expireAfterWrite(1000, TimeUnit.MILLISECONDS).build();

    private static Stu getStu(Long id) throws Exception {
        Stu stu = skuCache.getIfPresent(id);
        if (null != stu) {
            return stu;
        }
        //模拟回源加载数据需要时间,回源加载数据并放入缓存
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stu = new Stu(id, "name" + id);
        skuCache.put(id, stu);
        System.out.println("回源加载：" + id);
        return stu;
    }

    public static void main(String[] args) throws Exception {
        //三个线程并发请求同一个数据，会加载3次
        for (int i = 0; i < 3; i++) {
            new Thread(() -> {
                try {
                    System.out.println(getStu(1L));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    private static record Stu(Long id, String name) {
    }

}
