package fly4j.common.file;

import org.apache.commons.io.FilenameUtils;

/**
 * Created by qryc on 2021/9/7
 */
public class FilenameUtil {
    /**
     * baseDirFile：/export/资料/
     * baseDirFile：/export/资料
     * baseDirFile：/export/资料/
     * file :      /export/资料/文件/a.txt
     * 输出：文件/a.txt
     *
     * @param filePathStr
     * @param baseDirPathStr
     * @return
     */
    public static String getSubPathUnix(String filePathStr, String baseDirPathStr) {
        filePathStr = FilenameUtils.separatorsToUnix(filePathStr.toString());
        baseDirPathStr = FilenameUtils.separatorsToUnix(baseDirPathStr.toString());
        //文件或文件夹如果以/结尾，去除，因为名字不带/结尾，干脆统一都不以/结尾，需要统一再加，便于理解
        if (filePathStr.endsWith("/")) {
            filePathStr = filePathStr.substring(0, filePathStr.length() - 1);
        }
        if (baseDirPathStr.endsWith("/")) {
            baseDirPathStr = baseDirPathStr.substring(0, baseDirPathStr.length() - 1);
        }

        //如果baseDirPathStr不以/结尾和filePathStr相等，则返回""
        if (filePathStr.equals(baseDirPathStr)) {
            return "";
        }

        return filePathStr.replace(baseDirPathStr + "/", "");

    }

    public static String trimStartVirgule(String key) {
        if (key.startsWith("/") || key.startsWith("\\")) {
            key = key.substring(1);
        }
        return key;
    }
}
