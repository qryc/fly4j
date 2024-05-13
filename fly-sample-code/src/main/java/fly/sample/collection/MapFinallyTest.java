package fly.sample.collection;

import java.util.HashMap;
import java.util.Map;

public class MapFinallyTest {
    public static void main(String[] args) {
        System.out.println(getMap());

    }

    private static Map getMap() {
        var map = new HashMap<String, String>();
        try {
            map.put("a", "a1");
            return map;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            map.put("b", "b1");
        }
        return map;
    }
}
