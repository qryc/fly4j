package fly.sample.string;

public class StrMain {
    public static void main(String[] args) {
        System.out.println("a.b".contains(""));
        System.out.println("ab".contains(""));
        System.out.println("a\\b");
        System.out.println("a\\b".replaceAll("\\\\",","));
    }
}
