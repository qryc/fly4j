package fly4j.common.util;

/**
 * 用于处理已经有返回值,需要截断执行程序场景
 */
public class BreakException extends Exception{
    public BreakException() {
    }

    public BreakException(String message) {
        super(message);
    }
}
