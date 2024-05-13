package fly.sample.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

import java.util.List;

public class PipeLineVsTransIncrThread {

    public static void main(String[] args) throws Exception {
        int count = 100 * 10000;
        final Jedis jedis = new Jedis("127.0.0.1", 6379);
        jedis.set("name","100");
        long start = System.currentTimeMillis();
        //3个线程PipeLine扣减,扣减
//        for (int i = 0; i < 3; i++) {
//            new Thread(() -> {
//                try {
//                    testPipeline(jedis);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }).start();
//        }
        for (int i = 0; i < 10; i++) {
            testPipeline(jedis);
        }
        System.out.println("100WpipeLine耗时：" + (System.currentTimeMillis() - start));


    }

    public static void testPipeline(Jedis jedis) throws Exception {
        Pipeline p = jedis.pipelined();
        p.incrBy("name", 1);
        List<Object> list = p.syncAndReturnAll();
        System.out.println(list);

    }

}
