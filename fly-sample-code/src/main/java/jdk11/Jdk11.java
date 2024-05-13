package jdk11;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Jdk11 {
    public static void main(String[] args) {
        new11();

    }
    private static void old11() {
        var name = "pin1";
        var age = 23;
        System.out.println(name + age);

        List<Integer> list = new ArrayList<Integer>();
        list.add(1);
        list.add(5);
        list.add(3);
        System.out.println(list);

        System.out.println("takeWhile");
        Stream.of(1, 2, 3, 5, 4)
                .takeWhile(n -> n < 3).forEach(System.out::println);
        System.out.println("dropWhile");
        Stream.of(1, 2, 3, 1, 4)
                .dropWhile(n -> n < 3).forEach(System.out::println);
    }
    private static void new11() {
        var name = "pin1";
        var age = 23;
        System.out.println(name + age);

        var list = List.of(1, 5, 3);
        System.out.println(list);

        System.out.println("takeWhile");
        Stream.of(1, 2, 3, 5, 4)
                .takeWhile(n -> n < 3).forEach(System.out::println);
        System.out.println("dropWhile");
        Stream.of(1, 2, 3, 1, 4)
                .dropWhile(n -> n < 3).forEach(System.out::println);
    }
}
