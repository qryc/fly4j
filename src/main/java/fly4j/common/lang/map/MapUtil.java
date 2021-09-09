package fly4j.common.lang.map;

import fly4j.common.lang.FlyResult;
import fly4j.common.lang.StringConst;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.function.Predicate;

public class MapUtil {
    public static <K, V> LinkedHashMap<K, V> filterLinkedHashMap(LinkedHashMap<K, V> linkedHashMap, Predicate<Map.Entry<K, V>> predicate) {
        LinkedHashMap resultMap = new LinkedHashMap<>();
        for (Map.Entry entry : linkedHashMap.entrySet()) {
            if (predicate.test(entry)) {
                resultMap.put(entry.getKey(), entry.getValue());
            }
        }
        return resultMap;
    }

    public static <K, V> LinkedHashMap<V, List<K>> convert2ValueMap(Map<K, V> map) {
        LinkedHashMap<V, List<K>> valueMap = new LinkedHashMap<>();
        map.forEach((k, v) -> {
            List<K> keys = valueMap.computeIfAbsent(v, key -> new ArrayList<>());
            keys.add(k);
        });
        return valueMap;
    }



    /**
     * historyMd5MapRead 为基准
     */
    public static MapCompareResult compareTwoMap(Map<String, String> historyMd5MapRead, Map<String, String> currentMd5Map) {
        MapCompareResult result = new MapCompareResult();


        //遍历历史的，查找删除的Key
        historyMd5MapRead.forEach((oldKey, oValue) -> {
            result.count.addAndGet(1);
            String md5New = currentMd5Map.get(oldKey);
            if (null == md5New) {
                result.deleteKeys.add(oldKey);
            } else if (md5New.equals(oValue)) {

            } else {
                result.diffKeys.add(oldKey);
            }
        });

        //遍历当前的，查找新增
        currentMd5Map.forEach((cKey, cValue) -> {
            String md5History = historyMd5MapRead.get(cKey);
            if (null == md5History) {
                result.addKeys.add(cKey);
            }
        });
        return result;
    }

}
