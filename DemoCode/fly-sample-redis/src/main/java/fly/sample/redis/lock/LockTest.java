package fly.sample.redis.lock;

import redis.clients.jedis.Jedis;

public class LockTest {
    public static void main(String[] args) {
        String orderId = "order123456";
        Jedis jedis = new Jedis("127.0.0.1", 6379);
        //删除之前测试结果
        jedis.del("union_" + orderId);
        jedis.del("result_" + orderId);
        System.out.println(lockTest(orderId, jedis));
        System.out.println(lockTest(orderId, jedis));

    }

    private static String lockTest(String orderId, Jedis jedis) {
        //写入防重Key
        long result = jedis.setnx("union_" + orderId, "1");
        // 1 第一次请求
        if (result == 1) {
            //正常处理
            try {
                String dealResult = "";
                System.out.println("正常处理过程");
                dealResult = "处理结果：{X:XXXXXXX}";
                //处理完成，处理结果写入Redis
                jedis.set("result_" + orderId, dealResult);
                System.out.println("返回正常结果");
                return dealResult;
            } catch (Exception e) {
                //异常情况,删除防重Key
                jedis.del("union_" + orderId);
                throw new RuntimeException("异常，请重试");
            }
        } else {//重复请求
            //返回上次请求结果
            System.out.println("返回幂等取结果");
            return jedis.get("result_" + orderId);
        }
    }
}
