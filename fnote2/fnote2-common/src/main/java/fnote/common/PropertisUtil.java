package fnote.common;

public class PropertisUtil {
    public static String adjustPath(String rootPtahStr) {
        if (rootPtahStr.contains("user.home")) {
            rootPtahStr = rootPtahStr.replaceAll("user.home", System.getProperty("user.home"));
        }
        return rootPtahStr;
    }
}
