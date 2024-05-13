package fly.sample.lamda;

import java.util.function.Function;
import java.util.function.Predicate;

public class FunctionMain {
    public static void main(String[] args) {
        //Fuction 根据返回值泛型
        Function<Integer, String> function = str -> str + "end";
        System.out.println(function.apply(4));

        //Function 根据入参
        String str2 = fun1(str -> str + "end1", 5);
        System.out.println(str2);

        //条件入参
        testPredicate(str -> str.length() > 3, "abcd");
        testPredicate(str -> str.length() > 3, "a");
    }

    public static <T, R> R fun1(Function<T, R> function, T param) {
        return function.apply(param);
    }

    public static void testPredicate(Predicate<String> predicate, String str) {
        if (predicate.test(str)) {
            System.out.println(str + "true");
        } else {
            System.out.println(str + "false");
        }
    }

    @FunctionalInterface
    interface IA{
        public void test();
    }
}
