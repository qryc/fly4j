package fly4j.common.util.map;

import java.util.*;
import java.util.function.Predicate;

public class MapUtil {


    public static Boolean getBooleanDFalse(Map<String, String> map, String key) {
        if (null == map.get(key)) {
            return false;
        }
        return Boolean.valueOf(map.get(key));
    }


}
