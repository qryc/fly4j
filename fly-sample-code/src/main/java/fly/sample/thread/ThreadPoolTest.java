package fly.sample.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor 的内部实现
 */
public class ThreadPoolTest {
    public static void main(String[] args) throws Exception {
        BlockingQueue<Runnable> queue = createQueue(1);
        int corePoolSize = 4;
        int maxPoolSize = 4;
        int keepAliveSeconds = 300;
        ThreadPoolExecutor executor = new ThreadPoolExecutor(corePoolSize, maxPoolSize, (long) keepAliveSeconds, TimeUnit.SECONDS, queue, new MyThreadFactory(), new AbortPolicy());
        List<Future> futures = new ArrayList<>();
        long stime = System.currentTimeMillis();
        System.out.println("第一批");
        for (int i = 1; i <= 10; i++) {
            try {
                Future<Integer> future = executor.submit(new MyCallable(i));
                futures.add(future);
                System.out.println("提交成功：" + i);
            } catch (Exception e) {
                System.out.println("时间：" + (System.currentTimeMillis() - stime) + " " + "提交失败：" + i + " " + e.getMessage());
            }

        }
        Thread.sleep(200);
        System.out.println("第二批");
        for (int i = 11; i <= 20; i++) {
            try {
                Future<Integer> future = executor.submit(new MyCallable(i));
                futures.add(future);
                System.out.println("提交成功：" + i);
            } catch (Exception e) {
                System.out.println("时间：" + (System.currentTimeMillis() - stime) + " " + "提交失败：" + i + " " + e.getMessage());
            }

        }
        System.out.println(futures.size());
        for (Future future : futures) {
            System.out.println("get:" + future.get(105, TimeUnit.MILLISECONDS));
        }
    }

    protected static BlockingQueue<Runnable> createQueue(int queueCapacity) {
        return (BlockingQueue) (queueCapacity > 0 ? new LinkedBlockingQueue(queueCapacity) : new SynchronousQueue());
    }


    public static class AbortPolicy implements RejectedExecutionHandler {
        public AbortPolicy() {
        }

        public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
            throw new RejectedExecutionException("Task " + r.toString() +
                    " rejected from " +
                    e.toString());
        }
    }

    static class MyThreadFactory implements ThreadFactory {
        private final AtomicInteger nextId = new AtomicInteger(1);

        MyThreadFactory() {
        }

        public Thread newThread(Runnable runnable) {
            String name = "myThread" + nextId.getAndIncrement();
            Thread thread = new Thread(runnable, name);
            System.out.println("创建线程 Thread:" + thread.getName());
            return thread;
        }
    }

    static class MyCallable implements Callable {
        int result = 0;

        public MyCallable(int result) {
            this.result = result;
        }

        @Override
        public Object call() throws Exception {
            Thread.sleep(100);
            System.out.println("完成：" + result);
            return result;
        }
    }


}
