package fly.sample.thread;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 优化为多机抢占式,带可计算整体圈数
 * 其实是伪命题，没必要这么麻烦，不如使用方案2，直接从每个表的执行监控计算总时间（计算方法，所有表都有数据，取Max）
 */
public class Thread4TableLockLoop {
    public static void main(String[] args) {
        //通过调整并发数，控制每个docker的并发,线程数是托底线程数，每个docker最多出动多少线程
        ThreadPoolExecutor threadPoolTaskExecutor = new ThreadPoolExecutor(0, 16, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(0));

        //使用定时器触发
        //....

        //计算整体运行状态
        String totalLockKey = "totalLockKey";
        int runCount = 0;
        for (int startDbIndex = 0; startDbIndex < 10; startDbIndex++) {
            for (int tableIndex = 0; tableIndex < 128; tableIndex++) {
                String key = "" + startDbIndex + "_" + tableIndex;
                //从Redis中获取锁,如果其他人处理就不处理,预计10小时内托底，10小时后会二次执行
                if (RedisLock.exist(key)) {
                    runCount++;
                }
            }
        }
        //全在跑，设置标志，计时开始
        if (runCount == 1280) {
            //时间为明细托底时间，本key不需要
            RedisLock.set(totalLockKey, "1");
            //监控开始时间
            //important code
        }
        //全跑完
        if (runCount == 0) {
            //时间为托底时间
            RedisLock.delete(totalLockKey);
            //监控开始时间
            //important code
        }

        //如果上一圈没有跑完，退出
        if (RedisLock.exist(totalLockKey)) {
            return;
        }


        //最多每个Docker出动多少线程，会适当调大大于（总任务数/docker数），容灾
        int maxTask = 16;
        if (threadPoolTaskExecutor.getActiveCount() >= maxTask) {
            return;
        }


        //能执行到此处，说明没有
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
                        //doSomething
                    } finally {
                        //处理完后，删除Redis
                        RedisLock.delete(key);
                    }
                });
            }
        }


    }

    static class RedisLock {
        public static boolean lock(String key, long time) {
            // SET key value NX PX millisecond
            return true;
        }

        public static boolean set(String key, String value) {
            // SET key value
            return true;
        }

        public static void delete(String key) {
        }

        public static boolean exist(String key) {
            return true;
        }
    }
}
