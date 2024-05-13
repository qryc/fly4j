package fly.sample.collection;

import java.util.HashMap;
import java.util.Map;

public class MapPretty {
    public String one(String str) {
        if (str.equals("a")) {
            return "1";
        } else if (str.equals("b")) {
            return "2";
        } else if (str.equals("c")) {
            return "3";
        } else {
            throw new UnsupportedOperationException();
        }
    }


    public String tow(String str) {
        Map<String, String> map = new HashMap<>();
        map.put("a", "1");
        map.put("b", "2");
        map.put("c", "3");
        if (map.containsKey(str)) {
            return map.get(str);
        } else {
            throw new UnsupportedOperationException();
        }
    }
}
