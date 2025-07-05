package fnote.common;

public class LogUtil {
    public static void out(String pre, Object msg, int size) {
        System.out.print(pre);
        for (int i = 0; i < size - pre.length(); i++) {
            System.out.print("-");
        }
        if (msg != null) {
            System.out.println(msg.toString());
        } else {
            System.out.println("");
        }
    }
}
