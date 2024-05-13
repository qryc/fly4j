package fly.sample.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Transaction;

import java.util.List;
import java.util.Random;

public class PipeLineVsTransIncr {

    public static void main(String[] args) throws Exception {
        int count = 100 * 10000;

        Jedis jedis = new Jedis("127.0.0.1", 6379);
        long start = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            testPipeline(jedis, i);
        }
        System.out.println("100WpipeLine耗时：" + (System.currentTimeMillis() - start));

        jedis = new Jedis("127.0.0.1", 6379);
        start = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            testMulti(jedis, i);
        }
        System.out.println("100W事务耗时：" + (System.currentTimeMillis() - start));

    }

    public static void testPipeline(Jedis jedis, int i) throws Exception {
        Pipeline p = jedis.pipelined();
        p.incrBy("nameP", 1);
        p.setex("unionP_" + i, 30, "1");
        List<Object> list = p.syncAndReturnAll();

    }

    public static void testMulti(Jedis jedis, int i) throws Exception {
        Transaction p = jedis.multi();
        p.incrBy("nameT", 1);
        p.setex("unionT_" + i, 30, "1");
        List<Object> list = p.exec();
    }
}
