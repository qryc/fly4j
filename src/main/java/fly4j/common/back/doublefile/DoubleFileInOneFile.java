package fly4j.common.back.doublefile;

import fly4j.common.back.version.DigestType;
import fly4j.common.back.version.DirDigestCalculate;
import fly4j.common.back.version.DirVersionCheckParam;
import fly4j.common.file.FileUtil;
import fly4j.common.lang.map.MapUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

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
        LinkedHashMap<File, String> fileLengthMapAll = DirDigestCalculate.getDirDigestFileMap(compareDir, dirMd5Param);

        //转换Map：Key:文件长度，value：长度相等的文件列表
        LinkedHashMap<String, List<File>> lengthGroupFileMap_len = MapUtil.convert2ValueMap(fileLengthMapAll);

        //从ValueMap中查找重复文件
        LinkedHashMap<String, List<File>> doubleFileMapByLen_len = MapUtil.filterLinkedHashMap(lengthGroupFileMap_len, e -> e.getValue().size() > 1);
        //简化结构，统一放入，不用按长度分堆 ，以前算法保持分堆，后续的二次检验在分堆中查找
        List<File> doubleFilesByLen = new ArrayList<>();
        doubleFileMapByLen_len.forEach((lengthStr, files) -> {
            doubleFilesByLen.addAll(files);
        });
        //二次MD5检验

        //长度相等的一堆数据，生成新的反相Map
        LinkedHashMap<String, List<File>> md5RevertMap_md5 = DirDigestCalculate.getFilesMd5DoubleMap(doubleFilesByLen);

        //删选相等的
        LinkedHashMap<String, List<File>> doubleFileMapFromMd5_md5 = MapUtil.filterLinkedHashMap(md5RevertMap_md5, e -> e.getValue().size() > 1);

        //输出结果
        return doubleFileMapFromMd5_md5;
    }




}
