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
public class Test1_GuavaComonUse {
    public static void main(String[] args) throws Exception {
        //创建一个1000长度的缓存，10毫秒失效
        Cache<String, Stu> cache = CacheBuilder
                .newBuilder().maximumSize(1000).recordStats().expireAfterWrite(10, TimeUnit.MILLISECONDS)
                .build();
        //放入一个数据，key为1
        cache.put("1", new Stu("name1"));
        System.out.println(cache.size());
        System.out.println(cache.stats());
        //放入后直接取，有值
        System.out.println("放入之后getIfPresent：" + cache.getIfPresent("1"));
        System.out.println(cache.size());
        System.out.println(cache.stats());
        Thread.sleep(11);
        //休息11秒再取，已经失效，无值
        System.out.println("休息11毫秒，失效后getIfPresent：" + cache.getIfPresent("1"));
    }

    private static record Stu(String name) {
    }

    /**
     * 直接存取
     */
    private static void commonUse() throws Exception {

    }


}
