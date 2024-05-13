package fly.sample.exception;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by qryc on 2021/9/17
 */
public class ExceptionDemo {

    //遍历文件夹中的文件，输出文件的大小，File.size方法会抛出IOException，需要try-catch
    public static void exceptionUgly(Path dirPath) throws IOException {
        Files.walk(dirPath).forEach(path -> {
            try {
                System.out.println(Files.size(path));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

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

    public static void exceptionGrace(Path dirPath) throws IOException {
        Files.walk(dirPath).forEach(path -> wrapperRuntime(() -> System.out.println(Files.size(path))));
    }

    public static void exceptionGraceWithReturn(Path dirPath) throws IOException {
        Files.walk(dirPath).forEach(path -> {
            long size = wrapperRuntimeR(() -> Files.size(path));
            System.out.println(size);
        });
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
