package fly.sample.thread.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor 的内部实现
 */
public class SpringThreadPoolTest {
    public static void main(String[] args) throws Exception {
//        test1();
        //测试先进对列，线程是先进队列，队列慢了再会新增加线程
        test2();
    }

    private static void test2() throws InterruptedException, ExecutionException, TimeoutException {
        ApplicationContext factory = new ClassPathXmlApplicationContext("classpath:spring-pool.xml");
        ThreadPoolTaskExecutor executor = factory.getBean("threadPoolTaskExecutor2", ThreadPoolTaskExecutor.class);
        System.out.println("main:" + Thread.currentThread());
        for (int i = 0; i < 10; i++) {
            final int index = i;
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("getCorePoolSize:"+executor.getThreadPoolExecutor().getCorePoolSize()+" getActiveCount:"+executor.getThreadPoolExecutor().getActiveCount()+" getQueue().size():"+executor.getThreadPoolExecutor().getQueue().size());
            executor.execute(new Thread() {
                @Override
                public void run() {
                    System.out.println("t" + index + ":" + Thread.currentThread());
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    super.run();
                }

            });
        }
    }

    private static void test1() throws InterruptedException, ExecutionException, TimeoutException {
        ApplicationContext factory = new ClassPathXmlApplicationContext("classpath:spring-pool.xml");
        ThreadPoolTaskExecutor executor = factory.getBean("threadPoolTaskExecutor", ThreadPoolTaskExecutor.class);
        List<Future> futures = new ArrayList<>();
        long stime = System.currentTimeMillis();
        System.out.println("第一批");
        for (int i = 1; i <= 10; i++) {
            try {
                Future<Integer> future = executor.submit(new MyCallable(i));
                futures.add(future);
                System.out.println("时间：" + (System.currentTimeMillis() - stime) + " " + "提交成功：" + i + " " + executor.getThreadPoolExecutor());
            } catch (Exception e) {
                System.out.println("时间：" + (System.currentTimeMillis() - stime) + " " + "提交失败：" + i + " " + e.getMessage());
//                e.printStackTrace();
            }

        }
        Thread.sleep(205);
        System.out.println("第二批");
        for (int i = 11; i <= 20; i++) {
            try {
                Future<Integer> future = executor.submit(new MyCallable(i));
                futures.add(future);
                System.out.println("时间：" + (System.currentTimeMillis() - stime) + " " + "提交成功：" + i + " " + executor.getThreadPoolExecutor());
            } catch (Exception e) {
                System.out.println("时间：" + (System.currentTimeMillis() - stime) + " " + "提交失败：" + i + " " + e.getMessage());
//                e.printStackTrace();
            }

        }
        System.out.println(futures.size());
        for (Future future : futures) {
            System.out.println("时间：" + (System.currentTimeMillis() - stime) + " " + "get:" + future.get(105, TimeUnit.MILLISECONDS));
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
