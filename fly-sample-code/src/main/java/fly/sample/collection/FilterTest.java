package fly.sample.collection;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FilterTest {
    public static void main(String[] args) {
        List<User> users = new ArrayList();
        users.add(new User(1, "a"));
        users.add(new User(3, "c"));
        users.add(new User(2, "b"));
        //过滤filter-collect
        List<User> userListFilter = users.stream().filter(user -> user.id() > 1).collect(Collectors.toList());
        System.out.println("stream filter id>1:" + userListFilter);
    }
}
