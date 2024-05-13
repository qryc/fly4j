package fly.sample.util.concurrent;

import java.util.concurrent.atomic.AtomicBoolean;

public class CASTest {
    public static void main(String[] args) {
        AtomicBoolean dealing = new AtomicBoolean();
        for (int i = 0; i < 10; i++) {
            new Thread() {
                @Override
                public void run() {
                    System.out.println(dealing.compareAndSet(false, true));
                }
            }.start();
        }

    }

    public void demo() {
        AtomicBoolean dealing = new AtomicBoolean();
        //当原值是false的时候，设置为true，返回值是是否成功
        dealing.compareAndSet(false, true);
    }
}
