package fly.sample.string;

public class StrSplit {
    public static void main(String[] args) {
        System.out.println("".split("@").length);//输出1
        System.out.println("@".split("@").length);//输出0
        System.out.println("@ ".split("@").length);//输出2

    }
}
