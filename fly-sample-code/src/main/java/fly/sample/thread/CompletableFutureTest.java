package fly.sample.thread;

import java.util.concurrent.*;

public class CompletableFutureTest {
    public static void main(String[] args) throws Exception {
        ExecutorService executor = Executors.newCachedThreadPool();
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (1 == 1)
                throw new RuntimeException("sdfsdf");
            return "testResult2";
        }, executor);
        //get 可以设置超时时间
        try {
            System.out.println(completableFuture.get(5, TimeUnit.MILLISECONDS));
        } catch (Exception e) {
            e.printStackTrace();
        }
        //完成执行，缺点，没有设置超时时间，可以指定线程
        completableFuture.thenAccept(result -> {
            System.out.println(result);
        });
        //抛出异常执行
        completableFuture.exceptionally(e -> {
            System.out.println("exceptionally" + e);
            return null;
        });
//        ThreadPoolExecutor
//        simpleComplete();
    }

    private static void simpleComplete() throws InterruptedException, ExecutionException, TimeoutException {
        CompletableFuture completableFuture = new CompletableFuture();

        Object result = completableFuture.get(10, TimeUnit.SECONDS);
        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000 * 3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                completableFuture.complete(1);
            }
        }.start();
        System.out.println("end" + result);
    }
}
