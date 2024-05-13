package fly.sample.pretty;

import java.util.Map;

public class MethodAndProperty {
    class Complex {
        private Map<String, String> extParamMap;

        public String getA() {
            if (extParamMap == null) {
                return null;
            }
            return extParamMap.get("a");
        }

        public String getB() {
            if (extParamMap == null) {
                return null;
            }
            return extParamMap.get("b");
        }

        public String getC() {
            if (extParamMap == null) {
                return null;
            }
            return extParamMap.get("c");
        }
    }

    class Pretty {
        private Map<String, String> extParamMap;

        public String getExtParam(String key) {
            if (extParamMap == null) {
                return null;
            }
            return extParamMap.get(key);
        }
    }
}
