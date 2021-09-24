package fly4j.common.util.map;

import java.util.LinkedHashMap;
import java.util.function.Function;

/**
 * Created by qryc on 2021/9/9
 */
public class LinkedHashMapUtil {
    public static <K, V, R> LinkedHashMap<R, V> alterKey(LinkedHashMap<K, V> map, Function<K, R> function) {
        LinkedHashMap<R, V> newMap = new LinkedHashMap<>();
        map.forEach((k, v) -> {
            newMap.put(function.apply(k), v);
        });
        return newMap;
    }
}
