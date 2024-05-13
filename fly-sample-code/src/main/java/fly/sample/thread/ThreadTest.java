package fly.sample.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadTest {
    private static ExecutorService executorService = new ThreadPoolExecutor(5, 5, 0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(
                    100000
            ));

    public static void main(String[] args) throws Exception {
        int loopEmpy = 0;
        ThreadPoolExecutor threadPoolExecutor = ((ThreadPoolExecutor) executorService);
        while (true) {
            System.out.println();
            int queueSize = threadPoolExecutor.getQueue().size();
            System.out.print("当前排队线程数：" + queueSize);

            int activeCount = threadPoolExecutor.getActiveCount();
            System.out.print(" 当前活动线程数：" + activeCount);
            System.out.print(" 是否已经停止：" + threadPoolExecutor.isTerminated());
            long completedTaskCount = threadPoolExecutor.getCompletedTaskCount();
            System.out.print(" 执行完成线程数：" + completedTaskCount);

            long taskCount = threadPoolExecutor.getTaskCount();
            System.out.println(" 总线程数：" + taskCount);

            Thread.sleep(3000);

            if (activeCount == 0) {
                loopEmpy++;
            }
            if (loopEmpy >= 3) {
                loopEmpy = 0;
                AtomicInteger count = new AtomicInteger(0);
                for (int i = 0; i < 50; i++) {
                    executorService.execute(() -> {
                        System.out.print(count.incrementAndGet() + ",");
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        }
    }
}
