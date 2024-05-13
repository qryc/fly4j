package fly.sample.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 单机处理固定表
 */
public class Thread4TableSimple {
    public static void main(String[] args) {
        //通过调整并发数，控制每个docker的并发
        ThreadPoolExecutor threadPoolTaskExecutor = new ThreadPoolExecutor(5, 5, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(100000));
        //使用定时器触发
        //....

        //避免上次没跑完
        if (threadPoolTaskExecutor.getQueue().size() > 0
                || threadPoolTaskExecutor.getActiveCount() > 0) {
            return;
        }
        for (int dbIndex = 0; dbIndex < 10; dbIndex++) {
            for (int tableIndex = 0; tableIndex < 128; tableIndex++) {
                threadPoolTaskExecutor.submit(() -> {
                    //针对每个表的线程处理
                });
            }
        }

    }
}
