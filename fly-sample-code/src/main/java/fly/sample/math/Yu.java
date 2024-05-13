package fly.sample.math;

import java.util.concurrent.atomic.AtomicInteger;

public class Yu {
    public static void main(String[] args) {
        AtomicInteger counter = new AtomicInteger();
        System.out.println(counter.getAndIncrement()&256);
        for (int i = 0; i < 10; i++) {
            System.out.println(String.format("%s %% 3=",i) + (i % 3) + String.format(" %s & 3=",i) + + (i & 3));
        }
    }
}
