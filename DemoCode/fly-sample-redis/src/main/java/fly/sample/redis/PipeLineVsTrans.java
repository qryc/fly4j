package fly.sample.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Transaction;

import java.util.List;

public class PipeLineVsTrans {
   static String script="redis.call('SET',KEYS[1],ARGV[1]);redis.call('SET',KEYS[2],ARGV[2]);redis.call('GET',KEYS[1]);redis.call('GET',KEYS[2]);redis.call('GET',KEYS[2]); return 1";
    public static void main(String[] args) throws Exception {
        int count = 100 * 10000;
        Jedis jedis = new Jedis("127.0.0.1", 6379);
        long start = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            testPipeline(jedis);
        }
        System.out.println("100WpipeLine耗时：" + (System.currentTimeMillis() - start));

        jedis = new Jedis("127.0.0.1", 6379);
        start = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            testMulti(jedis);
        }
        System.out.println("100W事务耗时：" + (System.currentTimeMillis() - start));

        jedis = new Jedis("127.0.0.1", 6379);
        start = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            testEval(jedis);
        }
        System.out.println("100W脚本耗时：" + (System.currentTimeMillis() - start));

    }

    public static void testPipeline(Jedis jedis) throws Exception {
        Pipeline p = jedis.pipelined();
        p.set("name", "soso");
        p.set("name2", "soso2");
        p.get("name");
        p.get("name2");
        p.get("name2");
        List<Object> list = p.syncAndReturnAll();
    }

    public static void testMulti(Jedis jedis) throws Exception {
        Transaction p = jedis.multi();
        p.set("name", "soso");
        p.set("name2", "soso2");
        p.get("name");
        p.get("name2");
        p.get("name2");
        List<Object> list = p.exec();
    }
    public static void testEval(Jedis jedis) throws Exception {
        jedis.eval(script,2,"name","soso","name2","soso2");
    }
    public void test(){
        Jedis jedis = new Jedis("127.0.0.1", 6379);
        System.out.println(jedis.eval(script,2,"name","soso","name2","soso2"));
    }
}
