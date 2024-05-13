package fly.sample.collection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapComputeIfAbsentTest {
    public static void oldMethod() {
        Map<String, List<String>> map = new HashMap<>();
        List<String> list = map.get("key");
        if (list == null) {
            list = new ArrayList<>();
            map.put("key", list);
        }
        list.add("something");
        System.out.println(map);
    }

    private static void newMethod() {
        //Map不存在就新建
        Map<String, List<String>> map = new HashMap<>();
        List<String> list = map.computeIfAbsent("key", key -> new ArrayList<>());
        list.add("something");
        System.out.println("computeIfAbsent:" + map);
    }

    public static void main(String[] args) {
        oldMethod();
        newMethod();
    }


}
