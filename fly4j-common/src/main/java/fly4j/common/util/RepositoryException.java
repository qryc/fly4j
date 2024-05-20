package fly4j.common.util;

/**
 * Created by qryc on 2021/9/18
 */
public class RepositoryException extends Exception {
    public RepositoryException(Throwable cause) {
        super(cause);
    }

    public RepositoryException(String message, Throwable cause) {
        super(message, cause);
    }
    public RepositoryException(String message) {
        super(message);
    }
    public static <T> T wrapperR(ExceptionUtil.ExceptionWrapperR<T, Exception> throwingConsumer) throws RepositoryException {
        try {
            return throwingConsumer.doInTry();
        } catch (Exception ex) {
            throw new RepositoryException(ex);
        }
    }

    public static void wrapper(ExceptionUtil.ExceptionWrapper<Exception> throwingConsumer) throws RepositoryException {
        try {
            throwingConsumer.doInTry();
        } catch (Exception ex) {
            throw new RepositoryException(ex);
        }
    }
}
