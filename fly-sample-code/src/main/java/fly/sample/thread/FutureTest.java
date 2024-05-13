package fly.sample.thread;

import java.util.concurrent.*;

public class FutureTest {
    /**
     * 时间是和当前时间求和
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        test1();
    }

    private static void test1() throws InterruptedException, ExecutionException, TimeoutException {
        ExecutorService executor = Executors.newCachedThreadPool();
        Task task1 = new Task(1000);
        Task task2 = new Task(2000);
        Future<Integer> future1 = executor.submit(task1);
        Future<Integer> future2 = executor.submit(task2);
        int result1 = future1.get(1050,TimeUnit.MILLISECONDS);
        System.out.println(result1);
        int result2 = future2.get(1050,TimeUnit.MILLISECONDS);
        System.out.println(result2);
    }
    private static void test2() throws InterruptedException, ExecutionException, TimeoutException {
        ExecutorService executor = Executors.newCachedThreadPool();
        Task task1 = new Task(1000);
        Task task2 = new Task(2000);
        Future<Integer> future1 = executor.submit(task1);
        Future<Integer> future2 = executor.submit(task2);
//        int result1 = future1.get(1050,TimeUnit.MILLISECONDS);
//        System.out.println(result1);
        int result2 = future2.get(1050,TimeUnit.MILLISECONDS);
        System.out.println(result2);
    }


    static class Task implements Callable<Integer> {
        private long mill;

        public Task(long mill) {
            this.mill = mill;
        }

        @Override
        public Integer call() throws Exception {
            //模拟一个任务需要执行时间
            Thread.sleep(mill);
            return (int) mill;
        }
    }
}
