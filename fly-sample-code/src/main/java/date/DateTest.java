package date;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class DateTest {
    public static void main(String[] args) {
        //5分钟
        System.out.println( TimeUnit.MINUTES.toMillis(5));
        System.out.println( Duration.ofSeconds(1).toMillis());

    }
}
