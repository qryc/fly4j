package fly.sample.collection.map;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by qryc on 2021/9/22
 */
public class MapComputeDemo {
    public static void main(String[] args) {
        Map<String, Integer> map = new HashMap<>();
        map.put("a", 100);
        map.put("b", 100);
        map.compute("a", (k, v) -> {
            return v-10;
        });
        System.out.println(map);
    }
}
