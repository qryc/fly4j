package fly.sample.lamda;

public class Jdk8FunctionTempB {
    public static void main(String[] args) {
        //方法引用
        tempB tempB = str -> System.out.println("execute....." + str);
        tempB.start("testB");

        tempB tempB2 = System.out::println;
        tempB2.start("testB2");


    }

    @FunctionalInterface
    interface tempB {
        void execute(String str);

        default void start(String str) {
            System.out.println("before");
            execute(str);
            System.out.println("after");
        }
    }
}
