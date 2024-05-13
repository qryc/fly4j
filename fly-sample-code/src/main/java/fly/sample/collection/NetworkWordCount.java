package fly.sample.collection;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NetworkWordCount {
    public static void main(String[] args) {
        var lineStrs = """
                hello word count1
                hello word count2
                hello word count3
                hello word count4
                """;
        Stream<String> linesStream = Arrays.stream(lineStrs.split(System.lineSeparator()));
        Stream<String> wordsStream = linesStream.flatMap(line -> Arrays.stream(line.split(" ")));
        Map<String,Integer> wordCount=wordsStream.collect(Collectors.toMap(String::trim,str->1,(oldValue,newValue)->oldValue+newValue));
        wordCount.entrySet().stream().forEach(stringIntegerEntry -> System.out.println(stringIntegerEntry.getKey()+":"+stringIntegerEntry.getValue()));
    }
}
