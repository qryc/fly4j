package fly.sample.thread;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 优化为多机抢占式
 */
public class Thread4TableLock {
    public static void main(String[] args) {
        //通过调整并发数，控制每个docker的并发,托底线程数,
        ThreadPoolExecutor threadPoolTaskExecutor = new ThreadPoolExecutor(0, 16, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(0));
        //使用定时器触发
        //....

        //最多每个Docker出动多少线程，会适当调大大于（总任务数/docker数），容灾
        int maxTask = 16;
        if (threadPoolTaskExecutor.getActiveCount() >= maxTask) {
            return;
        }
        for (int startDbIndex = 0; startDbIndex < 10; startDbIndex++) {
            for (int tableIndex = 0; tableIndex < 128; tableIndex++) {
                String key = "" + startDbIndex + "_" + tableIndex;
                //从Redis中获取锁,如果其他人处理就不处理,预计10小时内托底，10小时后会二次执行
                if (!RedisLock.lock(key, TimeUnit.HOURS.toMillis(10))) {
                    continue;
                }

                threadPoolTaskExecutor.submit(() -> {
                    //针对每个表的线程处理
                    try {
                        //处理完后，删除Redis
                    } finally {
                        RedisLock.delete(key);
                    }
                });
            }
        }

    }

    /**
     * EX second ：设置键的过期时间为 second 秒。 SET key value EX second 效果等同于 SETEX key second value 。
     * PX millisecond ：设置键的过期时间为 millisecond 毫秒。 SET key value PX millisecond 效果等同于 PSETEX key millisecond value 。
     * NX ：只在键不存在时，才对键进行设置操作。 SET key value NX 效果等同于 SETNX key value 。
     * XX ：只在键已经存在时，才对键进行设置操作。
     */
    static class RedisLock {
        public static boolean lock(String key, long time) {
            // SET key value NX PX millisecond
            return true;
        }

        public static void delete(String key) {
        }
    }
}
