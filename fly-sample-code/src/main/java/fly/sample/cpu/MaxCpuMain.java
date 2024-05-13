package fly.sample.cpu;

import java.util.concurrent.TimeUnit;

public class MaxCpuMain {
    public static void main(String[] args) throws InterruptedException {
        //启动一个耗费CPU的线程，后续要通过工具找到这个线程
        new Thread(() -> {
            runMaxCpuCal();
        }).start();

        //启动N个干扰线程，占用CPU少
        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                runMinCpuCal();
            }).start();
        }

        //阻塞主程序不退出
        while (true) {
            Thread.sleep(100);
        }
    }

    //模拟高耗费CPU处理过程
    public static void runMaxCpuCal() {
        long count = 0;
        while (true) {
            count++;
            double a = Integer.MAX_VALUE + Math.random() * Integer.MAX_VALUE;
            long b = ("sdf" + a).hashCode();
            if (count % (10000 * 10000) == 0) {
                System.out.println("runMaxCpuCal run" + count);
            }
        }
    }

    //模拟低耗费CPU处理过程
    public static void runMinCpuCal() {
        long count = 0;
        while (true) {
            count++;
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (count % (30) == 0) {
                System.out.println("runMinCpuCal run" + count);
            }
        }
    }
}
