package fly.sample.lamda;

public class Jdk8Function1Thread {
    public static void main(String[] args) {
        //简单的函数式编程
        Runnable runnable = () -> System.out.println("abc");
        new Thread(runnable).start();
    }

}
