package fly.sample.lamda;

import java.util.Optional;

public class OptionalMain {
    public static void main(String[] args) {
        Integer count = 1;

        Integer result = Optional.ofNullable(count).map(countTemp -> ++countTemp).orElse(count = 9999);
        System.out.println(result);

        count = null;
        result = Optional.ofNullable(count).map(countTemp -> ++countTemp).orElse(count = 9999);
        System.out.println(result);

    }
}
