package fly.sample.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Transaction;

import java.util.List;

public class TestRedis {

    public static void main(String[] args) throws Exception {
        testJedis();
        testPipeline();
        testMulti();
    }

    public static void testJedis() throws Exception {
        Jedis jedis = new Jedis("127.0.0.1", 6379);
        jedis.set("name", "soso");//向key-->name中放入了value-->xinxin
        System.out.println(jedis.get("name"));
        System.out.println(jedis.incrBy("num", 1));

    }

    public void testJedisM() throws Exception {
//        final int maxCount = 2;
//        jedis.monitor(new JedisMonitor() {
//			int counts = 0;
//
//			@Override
//			public void onCommand(String command) {
//				counts++;
//				System.out.println(command);
//				if (counts > maxCount) {
//					jedis.disconnect();
//				}
//			}
//		});
//        System.out.println("end");

    }

    public static void testJedisConfigSet() throws Exception {
//        // normal 0 0 0 slave 268435456 67108864 60 pubsub 33554432 8388608 60
//        System.out.println(jedis.configGet("client-output-buffer-limit"));
//        jedis.configSet("client-output-buffer-limit", "normal 0 0 0 slave 0 0 0 pubsub 33554432 8388608 60");
//        System.out.println(jedis.configGet("client-output-buffer-limit"));

    }

    public static void testPipeline() throws Exception {
        Jedis jedis = new Jedis("127.0.0.1", 6379);
        Pipeline p = jedis.pipelined();
        p.set("name", "soso");//向key-->name中放入了value-->xinxin
        p.set("name2", "soso2");//向key-->name中放入了value-->xinxin
        p.get("name");
        p.get("name2");
        p.get("name2");
        List<Object> list = p.syncAndReturnAll();
        System.out.println(list);

    }

    public static void testMulti() throws Exception {
        Jedis jedis = new Jedis("127.0.0.1", 6379);
        Transaction p = jedis.multi();
        p.set("name", "soso");//向key-->name中放入了value-->xinxin
        p.set("name2", "soso2");//向key-->name中放入了value-->xinxin
        p.get("name");
        p.get("name2");
        p.get("name2");
        List<Object> list = p.exec();
        System.out.println(list);

    }

    public static void testJedis2() throws Exception {
//        jedis.hset("NUM_111", "2", "22");
//        jedis.hset("NUM_111", "3", "33");
////		System.out.println("dump"+jedis.dump("NUM_111"));
//        byte[] dump=jedis.dump("NUM_111");
//        ByteBuffer buf = ByteBuffer.wrap(dump);
//        buf.order(ByteOrder.LITTLE_ENDIAN);
//        byte dataType = buf.get();
//
//        System.out.println(jedis.restore("aa",100,dump));
//        System.out.println(jedis.hgetAll("aa"));
////		System.out.println("str"+new String(dump));
////		System.out.println("str"+jedis.hgetAll("NUM_111"));
//        Map<String,String> map=new HashMap<String, String>(100);
//        System.out.println(map.size());
    }

    public static void testJedis3() throws Exception {
//        jedis.set("name", "soso");//向key-->name中放入了value-->xinxin
//        assertEquals("soso", jedis.get("name"));
//        DebugParams params=DebugParams.OBJECT("s");
//        System.out.println(jedis.debug(params));
    }
}
