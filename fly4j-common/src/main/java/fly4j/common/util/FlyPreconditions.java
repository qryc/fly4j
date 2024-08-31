package fly4j.common.util;

import org.apache.commons.lang3.StringUtils;

public class FlyPreconditions {
    public static <T> T requireNotEmpty(T obj, String message) {
        if (obj == null) {
            throw new NullPointerException(message);
        }
        if (obj instanceof String && StringUtils.isBlank((String) obj)) {
            throw new IllegalArgumentException(message);
        }
        return obj;
    }

    public static <T> T requireNotEmpty(T obj) {
        return requireNotEmpty(obj, "Object must not be null or empty");
    }

    public static void requireArgumentFalse(boolean expression, String errorMsg) {
        if (expression) {
            throw new IllegalArgumentException(errorMsg);
        }
    }

    public static void requireArgumentTrue(boolean expression, String errorMsg) {
        if (!expression) {
            throw new IllegalArgumentException(errorMsg);
        }
    }

    public static void checkStateTrue(boolean expression, String errorMsg) {
        if (!expression) {
            throw new IllegalStateException(errorMsg);
        }
    }

    public static void checkStateFalse(boolean expression, String errorMsg) {
        if (expression) {
            throw new IllegalStateException(errorMsg);
        }
    }
}
