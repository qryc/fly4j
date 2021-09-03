package fly4j.common.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fly4j.common.lang.JsonUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * 本地配置测试
 * UserInfo: qryc
 */
public class TestJson {


    @Before
    public void setup() throws Exception {
    }

    /**
     * Main函数虽然不能自动测试，但阅读方便，作为教程比单元测试更加易读
     *
     * @param args
     */
    public static void main(String[] args) {
        UserWithMap userWithMap = new UserWithMap();
        userWithMap.setName("李白");
        Map<String, String> map = new HashMap<>();
        map.put("sex", "男");
        userWithMap.setMap(map);
        String jsonStr = JsonUtils.writeValueAsString(userWithMap);
        System.out.println(jsonStr);
        UserWithMap userWithMapFromJson = JsonUtils.readValue(jsonStr, UserWithMap.class);
        System.out.println(userWithMapFromJson);

        String jsonStr4Map = JsonUtils.writeValueAsString(userWithMap);
        System.out.println(jsonStr4Map);
        Map<String, String> mapFromJson = JsonUtils.readValue(jsonStr4Map,HashMap.class);
        System.out.println(mapFromJson);
    }

    @Test
    public void writeValueAsString2() throws Exception {

        User user = new User("pin1", 20, "sdf");
        String jsonStr = JsonUtils.writeValueAsString(user);
        Assert.assertEquals("{\"name\":\"pin1\",\"age\":20}", jsonStr);
        user = JsonUtils.readValue("{\"name\":\"pin1\",\"age\":20,\"age2\":20}", User.class);
        Assert.assertEquals("pin1", user.getName());
        Assert.assertEquals(20, user.getAge());

        //Map
        Map<String, String> map = new HashMap<>();
        map.put("a", "aa");
        jsonStr = JsonUtils.writeValueAsString(map);
        Assert.assertEquals("{\"a\":\"aa\"}", jsonStr);
        map = JsonUtils.readStringStringHashMap(jsonStr);
        Assert.assertEquals(1, map.size());
        Assert.assertEquals("aa", map.get("a"));

    }

    @After
    public void tearDown() throws Exception {

    }
}
