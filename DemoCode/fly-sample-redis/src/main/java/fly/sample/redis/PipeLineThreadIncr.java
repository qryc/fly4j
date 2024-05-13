package fly.sample.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Transaction;

import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class PipeLineThreadIncr {
    public static void main(String[] args) throws Exception {


        long start = System.currentTimeMillis();
        int threadSize = 10;
        AtomicInteger notExist = new AtomicInteger(0);
        CountDownLatch downLatch = new CountDownLatch(threadSize);
        for (int i = 0; i < threadSize; i++) {
            final int threadId = i;
            new Thread() {
                @Override
                public void run() {
                    testPipeLine(threadId, notExist);
                    downLatch.countDown();
                    super.run();
                }
            }.start();
        }
        downLatch.await();

        System.out.println("100WpipeLine耗时：" + (System.currentTimeMillis() - start));
        System.out.println("不存在总数：" + notExist.get());

    }

    public static void testPipeLine(int threadId, AtomicInteger notExist) {
        Jedis jedis = new Jedis("127.0.0.1", 6379);
        int count = 100 * 10000;
        for (int i = 0; i < count; i++) {
            Pipeline p = jedis.pipelined();
            p.incrBy("nameP", 1);
            String unionKey = "unionP_" + threadId + "_" + i;
            p.setex(unionKey, 5, "1");
            List<Object> list = p.syncAndReturnAll();

            //判断是否存在
            if (!jedis.exists(unionKey)) {
                notExist.incrementAndGet();
                System.out.println("thread%d不存在%d".formatted(threadId, i));
            }
        }
    }
}
