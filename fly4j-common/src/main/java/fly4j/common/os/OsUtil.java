package fly4j.common.os;

/**
 * 封装对操作系统相关命令，比如dump数据库
 * Created by qryc on 2015/9/10.
 */
public class OsUtil {
    public static boolean isLinuxLike() {
        return !isWindows();
    }

    public static boolean isWindows() {
        return getOsName().toLowerCase().startsWith("windows");
    }

    public static String getOsName() {
        return System.getProperty("os.name");
    }

    public static boolean isLinuxReal() {
        return getOsName().toLowerCase().startsWith("linux");
    }

    public static boolean isMac() {
        return getOsName().toLowerCase().startsWith("mac");
    }

    public static String getSimpleOsName() {
        if (isWindows()) {
            return "window";
        } else if (isMac()) {
            return "mac";
        } else if (isLinuxReal()) {
            return "linux";
        } else {
            return "linuxLike";
        }

    }

    public static void main(String[] args) {
        System.out.println(getOsName());
        System.out.println(OsUtil.isMac());
    }
}
