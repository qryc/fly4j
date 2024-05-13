package fly.sample.lamda;

public class Jdk8Function2Temp {
    public static void main(String[] args) {
        //简化模板方法
        tempA tempA = () -> System.out.println("execute.....");
        tempA.start();
    }

    @FunctionalInterface
    interface tempA {
        void execute();

        default void start() {
            System.out.println("before");
            execute();
            System.out.println("after");
        }
    }
}
