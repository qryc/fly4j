package fly.sample.collection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by qryc on 2021/9/9
 */
public class CollectionMap {
    static record User(int id, String name) {
    }

    public static void main(String[] args) {
        //准备测试数据
        List<User> users = new ArrayList();
        users.add(new User(1, "a"));
        users.add(new User(3, "c"));
        users.add(new User(2, "b"));

        {
            //转换元素类型
            System.out.println("filter id>1:" + users.stream().filter(user -> user.id > 1).map(user -> user.name).collect(Collectors.toList()));
            System.out.println("filter id>100:" + users.stream().filter(user -> user.id > 100).map(user -> user.name).findFirst().orElse("none"));

            System.out.println("stream map:" + users.stream().map(User::name).findFirst().get());
            System.out.println(users.stream().allMatch(user -> user.id > 0));
            System.out.println(users.stream().anyMatch(user -> user.id == 2));

        }


        //测试flatMap，
        {
            var lineStrs = """
                    hello word count1
                    hello word count2
                    hello word count3
                    hello word count4
                    """;
            System.out.println(Arrays.stream(lineStrs.split(System.lineSeparator()))
                    .flatMap(line -> Arrays.stream(line.split(" ")))
                    .collect(Collectors.toList()));
        }

    }
}
