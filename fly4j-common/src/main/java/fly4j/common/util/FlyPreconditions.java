package fly4j.common.util;

import org.apache.commons.lang3.StringUtils;

import java.util.Objects;
import java.util.function.Supplier;

public final class FlyPreconditions {
    private static final String DEFAULT_ERROR_MESSAGE = "Object must not be null or empty";

    private FlyPreconditions() {
        // Prevent instantiation
    }

    public static <T> T requireNotEmpty(T obj, String message) {
        return requireNotEmpty(obj, () -> message);
    }

    public static <T> T requireNotEmpty(T obj, Supplier<String> messageSupplier) {
        Objects.requireNonNull(obj, messageSupplier);
        if (obj instanceof String && StringUtils.isBlank((String) obj)) {
            throw new IllegalArgumentException(messageSupplier.get());
        }
        return obj;
    }

    public static <T> T requireNotEmpty(T obj) {
        return requireNotEmpty(obj, DEFAULT_ERROR_MESSAGE);
    }

    public static void requireArgument(boolean expression, String errorMsg) {
        if (!expression) {
            throw new IllegalArgumentException(errorMsg);
        }
    }

    public static void requireArgumentFalse(boolean expression, String errorMsg) {
        requireArgument(!expression, errorMsg);
    }

    public static void requireArgumentTrue(boolean expression, String errorMsg) {
        requireArgument(expression, errorMsg);
    }

    public static void checkState(boolean expression, String errorMsg) {
        if (!expression) {
            throw new IllegalStateException(errorMsg);
        }
    }

    public static void checkStateTrue(boolean expression, String errorMsg) {
        checkState(expression, errorMsg);
    }

    public static void checkStateFalse(boolean expression, String errorMsg) {
        checkState(!expression, errorMsg);
    }

    public static void checkNotNull(Object obj, String errorMsg) {
        if (obj == null) {
            throw new NullPointerException(errorMsg);
        }
    }
}
