package fly4j.common.cache;

import fly4j.common.cache.FlyCache;
import fly4j.common.cache.impl.FlyCacheJVM;
import org.junit.Assert;
import org.junit.Test;

public class TestFlyCache {


    @Test
    public void testCache() throws Exception {
        //创建一个大小位1000的缓存
        FlyCache flyCache = new FlyCacheJVM(1000);
        //放入一个元素
        flyCache.put("akey", "avalue", 2);
        //判断已经加入
        Assert.assertEquals("avalue", flyCache.get("akey"));
        //判断生命周期
        Assert.assertTrue(flyCache.ttl("akey") > 0);
        Thread.sleep(3);
        //判断已经过期
        Assert.assertNull(flyCache.get("akey"));
        Assert.assertTrue(flyCache.ttl("akey") < 0);
    }

    public void demo() {
        //1000 is the maxSize
        FlyCache flyCache = new FlyCacheJVM(1000);
        //2 is the cache life
        flyCache.put("a", "123", 2);
        // it will be return 123
        flyCache.get("akey");
        // it will be return left life
        flyCache.ttl("akey");
    }

}
