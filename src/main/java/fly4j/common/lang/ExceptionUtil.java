package fly4j.common.lang;

/**
 * Created by qryc on 2021/9/10
 */
public class ExceptionUtil {
    public static <T> void exceptionWrapper(
            ExceptionWrpper<T, Exception> throwingConsumer) {
        try {
            throwingConsumer.doInTry();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

    }

    @FunctionalInterface
    public static interface ExceptionWrpper<T, E extends Exception> {
        void doInTry() throws E;
    }

}
