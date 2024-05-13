package cache.guava;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 异步调用缓存测试，不同的key串行
 */
public class Test4_MulSyncLoadCache {


    public static void main(String[] args) throws Exception {
        //采用LoadingCache，如果缓存中不存在，执行对应的回调接口，此处返回的是一个带有当前线程的异步执行器
        LoadingCache<Long, User> asyncCacheUser = CacheBuilder.newBuilder().initialCapacity(10).maximumSize(10)
                .expireAfterWrite(3, TimeUnit.SECONDS).build(new CacheLoader<Long, User>() {
                    @Override
                    public User load(Long id) throws Exception {
                        //模拟加载数据需要时间
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        User user = new User(id, "name" + id);
                        System.out.println("回源load数据:" + user);
                        return user;
                    }
                });
        LoadingCache<Long, Stu> asyncCacheStu = CacheBuilder.newBuilder().initialCapacity(10).maximumSize(10)
                .expireAfterWrite(3, TimeUnit.SECONDS).build(new CacheLoader<Long, Stu>() {
                    @Override
                    public Stu load(Long id) throws Exception {
                        //模拟加载数据需要时间
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Stu stu = new Stu(id, "name" + id);
                        System.out.println("回源load数据:" + stu);
                        return stu;
                    }
                });
        //线程池
        ExecutorService executorService = Executors.newCachedThreadPool();


        //模拟三个并发，模拟Tomcat得到的3个不同的请求，不同的线程处理。
        for (int i = 1; i <= 3; i++) {
            int threadId = i;
            new Thread(() -> {
                try {
                    //开始取数据
                    System.out.println("请求线程%s开始异步获取数据".formatted(threadId));
                    Map<Long, Stu> stuMap = new HashMap<>();
                    Map<Long, User> userMap = new HashMap<>();
                    //模拟每个请求，使用异步线程来分别加载不同的数据
                    //加载用户
                    executorService.execute(() -> {
                        try {
                            userMap.put(1L, asyncCacheUser.get(1L));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                    //加载学生信息
                    executorService.execute(() -> {
                        try {
                            stuMap.put(1L, asyncCacheStu.get(1L));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                    //等待加载完成
                    Thread.sleep(1000);
                    //使用
                    System.out.println("请求线程%s开始使用数据".formatted(threadId));
                    System.out.println("请求线程%s开始使用数据 %s".formatted(threadId, userMap.get(1L)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }


    }

    private static record User(Long id, String name) {
    }

    private static record Stu(Long id, String name) {

    }


}
