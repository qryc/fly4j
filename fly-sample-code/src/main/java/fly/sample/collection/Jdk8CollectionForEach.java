package fly.sample.collection;

import java.util.*;
import java.util.stream.Collectors;

public class Jdk8CollectionForEach {
    static record User(int id,String name){
    }

    public void old() {


        List<User> users = new ArrayList();
        users.add(new User(1, "a"));
        users.add(new User(3, "c"));
        users.add(new User(2, "b"));

        List<String> names = new ArrayList<>();
        for (User user : users) {
            names.add(user.name());
        }
    }

    public static void main(String[] args) {
        //准备测试数据
        List<User> users = new ArrayList();
        users.add(new User(1, "a"));
        users.add(new User(3, "c"));
        users.add(new User(2, "b"));

        //遍历for-each
        users.forEach(user -> System.out.println(user));

        //转换元素类型
        List<String> names = users.stream().map(user -> user.name).collect(Collectors.toList());
        System.out.println("stream map:" + names);
        List<String> names2 = users.stream().map(User::name).collect(Collectors.toList());
        System.out.println("stream map:" + names2);


    }


}

