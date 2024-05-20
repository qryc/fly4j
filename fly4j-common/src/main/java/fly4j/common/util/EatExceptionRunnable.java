package fly4j.common.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 线程池
 * Created by qryc on 2018-01-11.
 */
@FunctionalInterface
public interface EatExceptionRunnable extends Runnable {
    static Logger log = LoggerFactory.getLogger(EatExceptionRunnable.class);

    @Override
    default void run() {
        try {
            this.runNoException();
        } catch (Exception e) {
            log.error("run Exception", e);
        }
    }

    void runNoException();
}
