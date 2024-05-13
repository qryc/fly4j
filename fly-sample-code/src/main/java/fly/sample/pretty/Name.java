package fly.sample.pretty;

/**
 * 建个常量SevenDay 或 OneWeeK，常量的赋值使用TimeUnit或者相乘赋值都可以，只执行一次，不会影响效率。
 */
public class Name {
    static class Complex {
        void sleep() throws Exception {
            Thread.sleep(604800);
        }
    }

    static class Pretty {
        static final int OneWeek = 604800;

        void sleep() throws Exception {
            Thread.sleep(OneWeek);
        }
    }

    public static void main(String[] args) throws Exception {
        //时间
        Thread.sleep(604800);
    }
}
