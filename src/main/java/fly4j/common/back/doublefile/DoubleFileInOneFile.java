package fly4j.common.back.doublefile;

import fly4j.common.back.version.DirDigestCalculate;
import fly4j.common.back.version.DigestType;
import fly4j.common.back.version.DirVersionCheckParam;
import fly4j.common.lang.MapUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.*;

/**
 * Created by qryc on 2021/8/25
 */
public class DoubleFileInOneFile {


    public static LinkedHashMap<String, List<File>> doubleFileCheck(String compareDir) {
        if (StringUtils.isBlank(compareDir)) {
            return new LinkedHashMap<>();
        }


        //取得文件长度
        DirVersionCheckParam dirMd5Param = new DirVersionCheckParam(DigestType.LEN, false, null);
        LinkedHashMap<File, String> fileLengthMapAll = DirDigestCalculate.getDirMd5FileMap(compareDir, dirMd5Param);
        //过滤Dir
        LinkedHashMap<File, String> fileLengthMap = MapUtil.filterLinkedHashMap(fileLengthMapAll, e -> !DirDigestCalculate.DIR_VALUE.equals(e.getValue()));

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
            String md5 = DirDigestCalculate.getMd5(file, DigestType.MD5);
            List<File> files = valueMap.computeIfAbsent(md5, key -> new ArrayList<>());
            files.add(file);
        });
        return valueMap;
    }

}
