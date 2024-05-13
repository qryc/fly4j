package fly.sample.json;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

public class JsonTest {
    public static ObjectMapper objectMapper = new ObjectMapper();

    private record User(long id, String name) {
    }

    public static void main(String[] args) {
        Map<String, String> map = new HashMap<>();
        map.put("a", "aa");
        String jsonStr1 = JsonUtils.writeValueAsString(map);
        System.out.println(jsonStr1);
        String json2 = JsonUtils.writeValueAsString(new User(1L, "libai"));
        System.out.println(json2);


    }
}
