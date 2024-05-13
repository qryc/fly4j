package fly.sample.pretty;

import java.util.*;

/**
 * 对等if-else不容易出错
 * 卫语句只用于入口的异常情况的防御式编程判断，比如空
 */
public class GurdIfElse {
    class Complex {
        public Set<String> getWhiteSetByAlias(String businessAlias) {
            Map<String, Set<String>> whiteListMap = new HashMap<>();
            if (whiteListMap != null) {
                if (whiteListMap.containsKey(businessAlias)) {
                    return whiteListMap.get(businessAlias);
                } else {
                    return new HashSet<String>();
                }
            } else {
                return new HashSet<>();
            }
        }
    }

    class Pretty {
        public Set<String> getWhiteSetByAlias(String businessAlias) {
            Map<String, Set<String>> whiteListMap = new HashMap<>();
            if (whiteListMap == null) {
                return new HashSet<>();
            }

            if (whiteListMap.containsKey(businessAlias)) {
                return whiteListMap.get(businessAlias);
            } else {
                return new HashSet<String>();
            }

        }
    }
}
