package fly.sample.collection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CollectionStream {
    List<User> user = new ArrayList<>();


    public static void main(String[] args) {
//        testMap();


//        testReduce();

        testFlatMap();
    }

    private static void testReduce() {
        //reduce运算拆解
        List<Integer> numbers = List.of(1, 2, 3);
        Integer sum = numbers.stream()
                .reduce(0, (a, b) -> a + b);
        System.out.println("reduce sum:" + sum);
    }

    private static void testFlatMap() {
        //testFlatMap
        List<String> words = Arrays.asList("java", "hello");
        var a = words.stream()
                .flatMap(word -> {
                    List<String> result = new ArrayList<>();
                    for (int i = 0; i < word.length(); i++) {
                        result.add(word.substring(i, i + 1));
                    }
                    return result.stream();
                }).collect(Collectors.toList());
        System.out.println(a);
    }

//    private static void testMap() {
//        List<User> users = getUsers();
//
//    }
//
//    private static List<User> getUsers() {
//
//    }


}

