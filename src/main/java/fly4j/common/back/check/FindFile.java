package fly4j.common.back.check;

import fly4j.common.lang.StringConst;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by qryc on 2021/9/6
 */
public class FindFile {
    public static void main(String[] args) {
        String checkDirStr = "";
        String findName = "target,classes";
        var result = findFile(checkDirStr, findName);
        System.out.println(result);
    }

    public static String findFile(String checkDirStr, String findName) {
        StringBuilder info = new StringBuilder("文件查找结果：").append(StringUtils.LF);
        Set<String> fileNameSet = new HashSet<>();
        Arrays.stream(findName.split(",")).forEach(name -> fileNameSet.add(name));
        info.append(" findName:").append(fileNameSet).append(StringUtils.LF);

        info.append(checkDirStr).append(" check:").append(StringUtils.LF);

        var files = FileUtils.listFilesAndDirs(new File(checkDirStr), TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
        files.stream().filter(file -> {
            for (String name : fileNameSet) {
                if (file.getName().equals(name)) {
                    return true;
                }
            }
            return false;
        }).forEach(file -> {
            StringConst.appendLine(info, file.getAbsolutePath() + " " + FileUtils.byteCountToDisplaySize(file.length()));
        });
        return info.toString();
    }


}
