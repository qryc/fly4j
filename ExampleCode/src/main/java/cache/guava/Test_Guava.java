package cache.guava;


import com.google.common.base.Ticker;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * 使用要点：
 * 1。如果在load中存在远程调用，一定要设置好超时时间。
 * 2。访问cache的时候使用getIfPresent，如果发现没有值，就使用降级策略的默认值，同时异步去用get或者refresh触发该key的加载过程，这样对于cache做到读写线程分离，让读的操作永远不会阻塞。
 * 3。如果再追求一点极致的设计的话，线程池异步触发load的操作也可以从读（线程）动作中解耦出来，我们可以使用queue作为中介，这样线程池异步触发load的操作进一步简化为“封装load操作为task，放入queue中”，
 * 节省了在读线程中有可能触发的启动线程的动作，同时只需要一个后台线程（池）去这个queue中拿任务处理即可。
 * ticker就是用来做测试用途，能够让我们改变时间源，模拟任何我们想要的时间流逝效果。
 */
public class Test_Guava {
    public static void main(String[] args) throws Exception {
//        loadTest();
    }
    private static record User(Long id, String name) {
    }





    private static void expireTest() throws Exception {
        /**
         * 如果缓存过期，恰好有多个线程读取同一个key的值，那么guava只允许一个线程去加载数据，其余线程阻塞。这虽然可以防止大量请求穿透缓存，但是效率低下。使用refreshAfterWrite可以做到：只阻塞加载数据的线程，其余线程返回旧数据。
         */
        LoadingCache<String, Stu> cacheLoad = CacheBuilder
                .newBuilder().maximumSize(1000).refreshAfterWrite(10, TimeUnit.MILLISECONDS)
                .build(new CacheLoader<String, Stu>() {
                    @Override
                    public Stu load(String s) throws Exception {
                        Thread.sleep(2);
                        return new Stu("name" + s + System.currentTimeMillis());
                    }
                });
        System.out.println("创建缓存：.refreshAfterWrite(10, TimeUnit.MILLISECONDS)");
//        cache.put("1", new Stu("name1"));
        System.out.println("放入之后getIfPresent：" + cacheLoad.get("1"));
        Thread.sleep(11);
        for (int i = 0; i < 10; i++) {
            new Thread() {
                @Override
                public void run() {
                    try {
                        System.out.println("休息11毫秒，失效后getIfPresent：" + cacheLoad.get("1"));
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }


    }

    private static void loadTest() throws ExecutionException {
        //普通使用，放入后有值

        Cache<String, Stu> cache = CacheBuilder
                .newBuilder().maximumSize(1000).expireAfterAccess(30, TimeUnit.MINUTES)
                .build();
        System.out.println("放入之前getIfPresent：" + cache.getIfPresent("1"));
        cache.put("1", new Stu("name1"));
        System.out.println("放入之后getIfPresent：" + cache.getIfPresent("1").name);

        //testload
        System.out.println("test load" + "_".repeat(10));
        LoadingCache<String, Stu> cacheLoad = CacheBuilder
                .newBuilder().maximumSize(1000).expireAfterAccess(30, TimeUnit.MINUTES)
                .build(new CacheLoader<String, Stu>() {
                    @Override
                    public Stu load(String s) throws Exception {
                        return new Stu("name" + s);
                    }
                });
        System.out.println("cacheLoad.getIfPresent(1)=%s".formatted(cacheLoad.getIfPresent("1")));
        System.out.println("cacheLoad.get(1).name=%s".formatted(cacheLoad.get("1").name));
        System.out.println("cacheLoad.getIfPresent(1)=%s".formatted(cacheLoad.getIfPresent("1").name));
//        cacheLoad = CacheBuilder
//                .newBuilder().maximumSize(1000).expireAfterAccess(30, TimeUnit.MINUTES)
//                .build((CacheLoader)(s) -> { new Stu("name" + s);});
    }


    private static record Stu(String name) {
    }

    public void test2() {
        // 自定义ticker
        TestTicker testTicker = new TestTicker();

        // 创建缓存,1小时没有访问则过期
        Cache<String, byte[]> cache = CacheBuilder.newBuilder().ticker(testTicker).expireAfterAccess(1, TimeUnit.HOURS).build();
        cache.put("id", new byte[1024 * 1024]);

        // 模拟时间流逝
        testTicker.addElapsedTime(TimeUnit.NANOSECONDS.convert(1, TimeUnit.HOURS));

        System.out.println(cache.getIfPresent("id") == null);//true

    }

    private static class TestTicker extends Ticker {
        private long start = Ticker.systemTicker().read();
        private long elapsedNano = 0;

        @Override
        public long read() {
            return start + elapsedNano;
        }

        public void addElapsedTime(long elapsedNano) {
            this.elapsedNano = elapsedNano;
        }
    }

}
