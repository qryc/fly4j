package cache.guava;

import com.google.common.util.concurrent.SettableFuture;

import java.util.concurrent.TimeUnit;

public class SettableFutureTest {
    public static void main(String[] args) throws Exception {
        SettableFuture<String> settableFuture = SettableFuture.create();
        settableFuture.set("ok");
//        settableFuture.setException(new RuntimeException("fail"));
        System.out.println(settableFuture.get(10, TimeUnit.MILLISECONDS));
    }
}
