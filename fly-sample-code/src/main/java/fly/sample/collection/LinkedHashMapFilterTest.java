package fly.sample.collection;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class LinkedHashMapFilterTest {
    public static void main(String[] args) {
        test4();
//        test3();
//        test4();
    }

    private static void test1() {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("a", "a1");
        map.put("aa", "a2");
        map.put("b", "b1");
        map.put("bb", "b2");
        System.out.println(map);

        //筛选出来包含1的，并保持有序
        map = map.entrySet().stream()
                .filter(e -> e.getValue().contains("1"))
                .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));
        System.out.println(map);
    }

    private static void test2() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("a", "a1");
        map.put("aa", "a2");
        map.put("b", "b1");
        map.put("bb", "b2");
        System.out.println(map);

        map = (LinkedHashMap) map.entrySet().stream()
                .filter(e -> e.getValue().contains("1"))
                .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));
        System.out.println(map);
    }

    private static void test3() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("a", "a1");
        map.put("aa", "a2");
        map.put("b", "b1");
        map.put("bb", "b2");
        System.out.println(map);

        map = filterLinkedHashMap(map, e -> e.getValue().contains("1"));
        System.out.println(map);
    }

    public static <K, V> LinkedHashMap<K, V> filterLinkedHashMap(LinkedHashMap<K, V> linkedHashMap, Predicate<Map.Entry<K, V>> predicate) {
        LinkedHashMap resultMap = new LinkedHashMap<>();
        for (Map.Entry entry : linkedHashMap.entrySet()) {
            if (predicate.test(entry)) {
                resultMap.put(entry.getKey(), entry.getValue());
            }
        }
        return resultMap;
    }

    private static void test4() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("a", "a1");
        map.put("aa", "a2");
        map.put("b", "b1");
        map.put("bb", "b2");
        System.out.println(map);

        map = map.entrySet()
                .stream().filter(e -> e.getValue().contains("1")).collect(Collectors.toMap(e -> e.getKey(), v -> v.getValue(),
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
        System.out.println("4" + map);
    }
}
