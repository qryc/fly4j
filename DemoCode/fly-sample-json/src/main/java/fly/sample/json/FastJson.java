package fly.sample.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FastJson {
    private static class User {

        private Long id;
        private String name;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static void main(String[] args) {
        Map<String, String> map = new HashMap<>();
        map.put("a", "aa");
        String jsonStr1 = JSON.toJSONString(map);
        System.out.println(jsonStr1);

        User guestUser = new User();
        guestUser.setId(2L);
        guestUser.setName("guest");

        String json2 = JSON.toJSONString(guestUser);
        System.out.println(json2);

        JSONObject p2=JSON.parseObject(json2);
        System.out.println(p2.get("id"));
    }
}
