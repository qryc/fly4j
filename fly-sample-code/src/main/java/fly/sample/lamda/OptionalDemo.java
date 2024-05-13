package fly.sample.lamda;

import java.util.List;
import java.util.Optional;

/**
 * Created by qryc on 2021/9/18
 */
public class OptionalDemo {
    public static void main(String[] args) {
        Optional<String> o1=Optional.empty();
        Optional<String> o2=Optional.of("a1");
        o1.ifPresent(name-> System.out.println(name));
        o2.ifPresent(name-> System.out.println(name));
        System.out.println(o1.get());
    }

}
