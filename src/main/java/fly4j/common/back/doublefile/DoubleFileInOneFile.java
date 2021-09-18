package fly4j.common.back.doublefile;

import fly4j.common.back.version.BackModel;
import fly4j.common.back.version.DirDigestCalculate;
import fly4j.common.file.FileAndDirPredicate;
import fly4j.common.lang.map.MapUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by qryc on 2021/8/25
 */
public class DoubleFileInOneFile {


    public static LinkedHashMap<String, List<File>> doubleFileCheck(String compareDir, Predicate<File> noNeedCalMd5FileFilter) {
        if (StringUtils.isBlank(compareDir)) {
            return new LinkedHashMap<>();
        }

        /**第一步，使用Length初筛**/
        //取得文件长度的Map
        LinkedHashMap<File, String> fileLengthMapAll = DirDigestCalculate.getDirDigestFileMap(compareDir, BackModel.DigestType.LEN, noNeedCalMd5FileFilter);
        //转换Map：Key:文件长度，value：长度相等的文件列表
        LinkedHashMap<String, List<File>> lengthGroupFileMap_len = MapUtil.convert2ValueMap(fileLengthMapAll);
        //从ValueMap中查找重复文件
        LinkedHashMap<String, List<File>> doubleFileMapByLen_len = MapUtil.filterLinkedHashMap(lengthGroupFileMap_len, e -> e.getValue().size() > 1);
        //简化结构，统一放入，不用按长度分堆 ，以前算法保持分堆，后续的二次检验在分堆中查找
        List<File> doubleFilesByLen = doubleFileMapByLen_len.entrySet().stream().flatMap(e -> e.getValue().stream()).collect(Collectors.toList());


        /**第二步，二次MD5检验**/
        //长度相等的一堆数据，生成新的反相Map
        LinkedHashMap<String, List<File>> md5RevertMap_md5 = DirDigestCalculate.getFilesMd5DoubleMap(doubleFilesByLen);
        //删选MD5相等的
        return MapUtil.filterLinkedHashMap(md5RevertMap_md5, e -> e.getValue().size() > 1);

    }


}
