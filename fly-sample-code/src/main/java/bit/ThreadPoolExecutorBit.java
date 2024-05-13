package bit;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * ThreadPoolExecutor 中用到的bit
 * java.util.concurrent.ThreadPoolExecutor
 */
public class ThreadPoolExecutorBit {
    //ctl的值倍拆分为了两部分
    private final AtomicInteger ctl = new AtomicInteger(ctlOf(RUNNING, 0));
    private static final int COUNT_BITS = Integer.SIZE - 3;
    private static final int COUNT_MASK = (1 << COUNT_BITS) - 1;

    // runState is stored in the high-order bits
    //RUNNING -1状态放入高三位 11100000000000000000000000000000
    private static final int RUNNING = -1 << COUNT_BITS;
    //SHUTDOWN 0状态放入高三位 00000000000000000000000000000000
    private static final int SHUTDOWN = 0 << COUNT_BITS;
    //STOP 1状态放入高三位 00100000000000000000000000000000
    private static final int STOP = 1 << COUNT_BITS;
    //TIDYING 2状态放入高三位 01000000000000000000000000000000
    private static final int TIDYING = 2 << COUNT_BITS;
    //TERMINATED 3状态放入高三位 01100000000000000000000000000000
    private static final int TERMINATED = 3 << COUNT_BITS;

    // Packing and unpacking ctl
    //根据int值的低29位解析出状态，~COUNT_MASK=11100000000000000000000000000000
    private static int runStateOf(int c) {
        return c & ~COUNT_MASK;
    }

    //根据int值的高3位解析出计数，COUNT_MASK=00011111111111111111111111111111
    private static int workerCountOf(int c) {
        return c & COUNT_MASK;
    }

    //反向解析，传入运行状态和计数，得到int值
    private static int ctlOf(int rs, int wc) {
        return rs | wc;
    }
    /*
     * Bit field accessors that don't require unpacking ctl.
     * These depend on the bit layout and on workerCount being never negative.
     */

    private static boolean runStateLessThan(int c, int s) {
        return c < s;
    }

    private static boolean runStateAtLeast(int c, int s) {
        return c >= s;
    }

    private static boolean isRunning(int c) {
        return c < SHUTDOWN;
    }

    /**
     * Attempts to CAS-increment the workerCount field of ctl.
     */
    private boolean compareAndIncrementWorkerCount(int expect) {
        return ctl.compareAndSet(expect, expect + 1);
    }

    /**
     * Attempts to CAS-decrement the workerCount field of ctl.
     */
    private boolean compareAndDecrementWorkerCount(int expect) {
        return ctl.compareAndSet(expect, expect - 1);
    }

    /**
     * Decrements the workerCount field of ctl. This is called only on
     * abrupt termination of a thread (see processWorkerExit). Other
     * decrements are performed within getTask.
     */
    private void decrementWorkerCount() {
        ctl.addAndGet(-1);
    }
    /*
     * Methods for setting control state
     */

    /**
     * Transitions runState to given target, or leaves it alone if
     * already at least the given target.
     *
     * @param targetState the desired state, either SHUTDOWN or STOP
     *                    (but not TIDYING or TERMINATED -- use tryTerminate for that)
     */
    private void advanceRunState(int targetState) {
        // assert targetState == SHUTDOWN || targetState == STOP;
        for (; ; ) {
            int c = ctl.get();
            if (runStateAtLeast(c, targetState) ||
                    //同时设置状态和计数，并发安全的事务保证
                    ctl.compareAndSet(c, ctlOf(targetState, workerCountOf(c))))
                break;
        }
    }

    public static void main(String[] args) {
        //高3位保存runState，低29位保存workerCount
        //左转移29位，也就是使用最后两位来表示运行状态，左边是高位，右边是低位
        System.out.println("%s RUNNING : %s".formatted(IntegerBit.toBinaryString(-1), IntegerBit.toBinaryString(RUNNING)));
        System.out.println("%s SHUTDOWN : %s".formatted(IntegerBit.toBinaryString(0), IntegerBit.toBinaryString(SHUTDOWN)));
        System.out.println("%s STOP : %s".formatted(IntegerBit.toBinaryString(1), IntegerBit.toBinaryString(STOP)));
        System.out.println("%s TIDYING : %s".formatted(IntegerBit.toBinaryString(2), IntegerBit.toBinaryString(TIDYING)));
        System.out.println("%s TERMINATED : %s".formatted(IntegerBit.toBinaryString(3), IntegerBit.toBinaryString(TERMINATED)));

        System.out.println(IntegerBit.toBinaryString(3));
        System.out.println(Integer.toBinaryString(TERMINATED));
        System.out.println(Integer.toBinaryString(workerCountOf(TERMINATED)));

        //运行状态取前三位，运行计数取后29位
        System.out.println("runStateOf---&-~COUNT_MASK : %s".formatted(IntegerBit.toBinaryString(~COUNT_MASK)));
        System.out.println("workerCountOf-&-COUNT_MASK : %s".formatted(IntegerBit.toBinaryString(COUNT_MASK)));

    }


}
