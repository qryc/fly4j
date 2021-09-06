package fly4j.common.back;

import fly4j.common.back.model.VersionType;
import fly4j.common.back.param.DirVersionCheckParam;
import fly4j.common.file.DirMd5Calculate;
import fly4j.common.lang.MapUtil;
import fly4j.common.lang.StringConst;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.*;

/**
 * Created by qryc on 2021/8/25
 */
public class BackTools {
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

    public static LinkedHashMap<String, List<File>> doubleFileCheck(String compareDir) {
        if (StringUtils.isBlank(compareDir)) {
            return new LinkedHashMap<>();
        }


        //取得文件长度
        DirVersionCheckParam dirMd5Param = new DirVersionCheckParam(VersionType.LEN, false, null);
        LinkedHashMap<File, String> fileLengthMapAll = DirMd5Calculate.getDirMd5FileMap(compareDir, dirMd5Param);
        //过滤Dir
        LinkedHashMap<File, String> fileLengthMap = MapUtil.filterLinkedHashMap(fileLengthMapAll, e -> !DirMd5Calculate.DIR_VALUE.equals(e.getValue()));

        //转换Map：Key:文件长度，value：长度相等的文件列表
        LinkedHashMap<String, List<File>> lengthGroupFileMap = MapUtil.convert2ValueMap(fileLengthMap);

        //从ValueMap中查找重复文件
        LinkedHashMap<String, List<File>> doubleFileMapFromLen = MapUtil.filterLinkedHashMap(lengthGroupFileMap, e -> e.getValue().size() > 1);

        //二次MD5检验
        LinkedHashMap<String, List<File>> doubleFileMapFromMd5 = new LinkedHashMap<>();
        doubleFileMapFromLen.forEach((lengthStr, filesParam) -> {
            //生成新的反相Map
            LinkedHashMap<String, List<File>> md5RevertMap = getMd5RevertMap(filesParam);

            md5RevertMap.forEach((md5, files) -> {
                if (files.size() > 1) {
                    doubleFileMapFromMd5.put(md5, files);
                }
            });
        });

        //输出结果
        return doubleFileMapFromMd5;
    }

    private static LinkedHashMap<String, List<File>> getMd5RevertMap(List<File> filesParam) {
        LinkedHashMap<String, List<File>> valueMap = new LinkedHashMap<>();
        filesParam.forEach(file -> {
            String md5 = DirMd5Calculate.getMd5(file, VersionType.MD5);
            List<File> files = valueMap.computeIfAbsent(md5, key -> new ArrayList<>());
            files.add(file);
        });
        return valueMap;
    }

}
