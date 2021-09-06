package fly4j.common.back.check;

import fly4j.common.lang.StringConst;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.List;

/**
 * 用于查找大文件
 * Created by qryc on 2016/5/4.
 */
public class MaxFile {


    public static String findMaxFileAndFloder(File dirFile, long alertFileSize, long alertFloderSize) {
        StringBuilder builder = new StringBuilder();
        File[] files = dirFile.listFiles();
        if (null == files) {
            return builder.toString();
        }
        for (File file : files) {

            if (file.isDirectory()) {
                //查找大文件夹
//                long size = FileUtils.sizeOfDirectory(file);
//                if (size > alertFloderSize) {
//                    StringConst.appendLine(builder, "大文件夹：" + file.getAbsolutePath() + " " + FileUtils.byteCountToDisplaySize(size));
//                }
                //递归
                String str = findMaxFileAndFloder(file, alertFileSize, alertFloderSize);
                if (StringUtils.isNotBlank(str))
                    StringConst.appendLine(builder, str);
            } else {
                //查找大文件
                long size = file.length();
                if (size > alertFileSize) {
                    StringConst.appendLine(builder, file.getAbsolutePath() + " " + FileUtils.byteCountToDisplaySize(size) + " " + " max than " + FileUtils.byteCountToDisplaySize(alertFileSize));
                }
            }

        }
        return builder.toString();

    }
    public static String maxFile(List<String> checkDirs, int bigFileSize) {
        StringBuilder info = new StringBuilder("检查大文件结果：").append(StringUtils.LF);
        checkDirs.forEach(checkDir -> {
            info.append(checkDir).append(" check:").append(StringUtils.LF);
            int limit = bigFileSize;

            var files = FileUtils.listFiles(new File(checkDir), null, true);
            files.stream().filter(file -> !file.isDirectory()).sorted((f1, f2) -> (int) (f2.length() - f1.length())).limit(limit).forEach(file -> {
                StringConst.appendLine(info, file.getAbsolutePath() + " " + FileUtils.byteCountToDisplaySize(file.length()));
            });
        });
        return info.toString();
    }


}
