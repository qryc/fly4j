package cache.guava;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 异步调用缓存测试，不同的key串行
 */
public class Test5_MulSyncLoadCache {

    //采用LoadingCache，如果缓存中不存在，执行对应的回调接口，此处返回的是一个带有当前线程的异步执行器
    private static LoadingCache<Long, MyCompletableFuture<User>> asyncCacheUser = CacheBuilder.newBuilder().initialCapacity(10).maximumSize(10)
            .expireAfterWrite(3, TimeUnit.SECONDS).build(new CacheLoader<Long, MyCompletableFuture<User>>() {
                @Override
                public MyCompletableFuture<User> load(Long id) throws Exception {
                    //模拟加载数据需要时间
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    MyCompletableFuture<User> future = new MyCompletableFuture<User>(Thread.currentThread());
                    System.out.println("回源load-Future:" + future);
                    return future;
                }
            });
    private static LoadingCache<Long, MyCompletableFuture<Stu>> asyncCacheStu = CacheBuilder.newBuilder().initialCapacity(10).maximumSize(10)
            .expireAfterWrite(3, TimeUnit.SECONDS).build(new CacheLoader<Long, MyCompletableFuture<Stu>>() {
                @Override
                public MyCompletableFuture<Stu> load(Long id) throws Exception {
                    //模拟加载数据需要时间
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    MyCompletableFuture<Stu> stu = new MyCompletableFuture<Stu>(Thread.currentThread());
                    System.out.println("回源load-Future:" + stu);
                    return stu;
                }
            });
    //线程池
    private static ExecutorService executorService = Executors.newCachedThreadPool();

    public static void main(String[] args) throws Exception {
        //模拟三个并发，模拟Tomcat得到的3个不同的请求，不同的线程处理。
        for (int i = 1; i <= 3; i++) {
            int threadId = i;
            new Thread(() -> {
                try {
                    //开始取数据
                    System.out.println("请求线程%s开始异步获取数据".formatted(threadId));
                    Map<Long, MyCompletableFuture<User>> userMap = new HashMap<>();
                    Map<Long, MyCompletableFuture<Stu>> stuMap = new HashMap<>();
                    //模拟每个请求，使用异步线程来分别加载不同的数据
                    //加载用户
                    executorService.execute(() -> {
                        try {
                            userMap.put(1L, getUserWithCache(1L));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                    //加载学生信息
                    executorService.execute(() -> {
                        try {
                            stuMap.put(1L, getStuWithCache(1L));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                    //等待加载完成
                    Thread.sleep(1000);
                    //开始使用数据执行逻辑，已经加载完成，不用等待很久
                    System.out.println("请求线程%s开始使用数据 %s".formatted(threadId, userMap.get(1L).get(10, TimeUnit.MILLISECONDS)));
                    System.out.println("请求线程%s开始使用数据 %s".formatted(threadId, stuMap.get(1L).get(10, TimeUnit.MILLISECONDS)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    private static MyCompletableFuture<User> getUserWithCache(Long key) throws Exception {
        //第一步从guava中取得异步加载对象，分别传入三个线程
        MyCompletableFuture<User> completableFuture = asyncCacheUser.get(key);
        //只有一个线程可以从缓存加载，并返回结果，其他的直接返回异步加载对象
        if (completableFuture.shouldProduce()) {
            //模拟线程加载数据
            //模拟需要时间
            Thread.sleep(100);
            User user = new User(key, "name" + key);
            completableFuture.complete(user);
            System.out.println("线程%s加载用户:%s".formatted(Thread.currentThread(), user));
        }
        return completableFuture;
    }

    private static MyCompletableFuture<Stu> getStuWithCache(Long key) throws Exception {
        //第一步从guava中取得异步加载对象，分别传入三个线程
        MyCompletableFuture<Stu> completableFuture = asyncCacheStu.get(key);
        //只有一个线程可以从缓存加载，并返回结果，其他的直接返回异步加载对象
        if (completableFuture.shouldProduce()) {
            //模拟线程加载数据
            //模拟需要时间
            Thread.sleep(100);
            Stu stu = new Stu(key, "name" + key);
            completableFuture.complete(stu);
            System.out.println("线程%s加载学生:%s".formatted(Thread.currentThread(), stu));
        }
        return completableFuture;
    }

    private static record User(Long id, String name) {
    }

    private static record Stu(Long id, String name) {

    }

    static class MyCompletableFuture<V> extends CompletableFuture<V> {
        //创建Future的线程，只有创建的线程可以执行加载
        private Thread thread;
        //CAS防护
        private AtomicBoolean dealing = new AtomicBoolean();

        public MyCompletableFuture(Thread thread) {
            this.thread = thread;
        }


        public boolean shouldProduce() {
            //只有调用当前的线程等于传入的线程可以执行
            return this.thread.equals(Thread.currentThread()) && dealing.compareAndSet(false, true) && !this.isDone();
        }

    }

}
