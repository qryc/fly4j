package async;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.*;

public class AsyncTest {

    static LoadingCache<String, ListenableFuture<String>> stuCache = null;

    static {
        //异步加载的loader
        CacheLoader<String, ListenableFuture<String>> cacheLoader = new CacheLoader() {
            public Object load(Object o) throws Exception {
                SettableFuture promiseFuture = SettableFuture.create();
                FutureCallback retryCallBack = new FutureCallback() {
                    @Override
                    public void onSuccess(Object o) {
                        promiseFuture.set(o);
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        promiseFuture.setException(throwable);
                    }
                };

                ListenableFuture firstFuture = getSkuFromRedis("test1");//redis请求
                Futures.addCallback(firstFuture, retryCallBack);


                return promiseFuture;
            }
        };
        //创建cache
        stuCache = Caffeine.newBuilder()
                .maximumSize(100)
                .expireAfterWrite(Duration.ofSeconds(30))
                .build(cacheLoader);

    }

    public static void main(String[] args) throws Exception {
        /**
         * 使用loadCache进行异步加载，缓存本身返回Future
         * 疑问：AsyncLoadingCache和LoadingCache
         */
        loadCache_Rpc();
        loadCache_Rpc();


    }

    public static void loadCache_Rpc2() throws Exception {
        //发起请求
        ThreadPoolExecutor executor = new ThreadPoolExecutor(100, 100, 100, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(100));
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(
                () -> {
                    return getStudentFromRpc("stu");
                }, executor);

        //得到结果
        completableFuture.thenAccept(str -> {
            System.out.println(str);
        });
        completableFuture.exceptionally(throwable -> {
            System.out.println("sdf");
            return null;
        });
    }

    /**
     * 使用loadCache进行异步加载，缓存本身返回Future
     * 疑问：AsyncLoadingCache和LoadingCache
     */
    public static void loadCache_Rpc() throws Exception {

        //发起异步请求，主线程直接往后走
        ListenableFuture<String> resultFutrue = stuCache.get("stu");
        //等待结果
        String sku = resultFutrue.get(10, TimeUnit.MILLISECONDS);
        System.out.println("sku:" + sku);

    }

    private static String getStudentFromRpc(String stu) {
        return "sdf";
    }

    private static ListenableFuture<String> getSkuFromRedis(String sku) {
        SettableFuture promiseFuture = SettableFuture.create();

        promiseFuture.set("sku" + sku);
        System.out.println("getSkuFromRedis:" + sku);
        return promiseFuture;
    }
}
