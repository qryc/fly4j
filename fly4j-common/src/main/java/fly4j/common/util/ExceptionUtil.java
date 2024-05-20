package fly4j.common.util;

/**
 * Created by qryc on 2021/9/10
 */
public class ExceptionUtil {
    @FunctionalInterface
    public static interface ExceptionWrapper<E extends Exception> {
        void doInTry() throws E;
    }

    public static void wrapperRuntime(ExceptionWrapper<Exception> throwingConsumer) {
        try {
            throwingConsumer.doInTry();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @FunctionalInterface
    public static interface ExceptionWrapperR<R, E extends Exception> {
        R doInTry() throws E;
    }

    public static <T> T wrapperRuntimeR(ExceptionWrapperR<T, Exception> throwingConsumer) {
        try {
            return throwingConsumer.doInTry();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
