package fly.sample.collection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class MapReduceTest {
    public static void main(String[] args) {
        //reduce运算拆解
        Integer sum = List.of(1, 2, 3).stream()
                .reduce((a, b) -> a + b).get();
        System.out.println("reduce sum:" + sum);


        //map
        List<String> words = List.of("java", "hello");
        List a = words.stream()
                .flatMap(word -> {
                    List<String> result = new ArrayList<>();
                    for (int i = 0; i < word.length(); i++) {
                        result.add(word.substring(i, i + 1));
                    }
                    return result.stream();
                }).collect(Collectors.toList());
        System.out.println(a);

        //第二个函数式累加器，非并行第三个参数没有用
        List<String> wordsB = words.stream().reduce(new ArrayList<>(), (list, str) -> {
            list.add(str);
            return list;
        }, (list1, list2) -> null);
        System.out.println(wordsB);

        wordsB = words.stream().reduce(new ArrayList<>(), (list, str) -> {
            list.add(str);
            return list;
        }, (list1, list2) -> {
            list1.addAll(list2);
            return list1;
        });
        System.out.println(wordsB);
    }
}
