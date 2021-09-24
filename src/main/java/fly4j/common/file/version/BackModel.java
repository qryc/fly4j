package fly4j.common.file.version;

import fly4j.common.file.FilenameUtil;

import java.io.File;
import java.util.*;
import java.util.function.Predicate;

/**
 * Created by qryc on 2021/9/3
 */
public class BackModel {
    public enum DigestType {
        MD5, LEN
    }


    public static record DirVersionGenParam(String checkBaseDirStr, Predicate<File> refusePredicate,
                                            String checkDate) {
    }


    public static record FileDigestModel(String pathStr, Long length, String md5) {
    }


    /**
     * 整个文件夹信息摘要，包含里面的文件与文件夹
     * Created by qryc on 2021/9/3
     */
    public static record DirDigestAllModel(Map<String, String> environment,
                                           String checkBaseDirStr,
                                           List<FileDigestModel> fileDigestModels) {

        public Map<String, String> getFilesDigestMap(DigestType versionType) {
            Map<String, String> map = new LinkedHashMap<>();
            for (FileDigestModel fileDigestModel : fileDigestModels) {
                var dirKey = FilenameUtil.getSubPathUnix(fileDigestModel.pathStr(), checkBaseDirStr);
                if (DigestType.LEN.equals(versionType)) {
                    map.put(dirKey, "" + fileDigestModel.length());
                } else {
                    map.put(dirKey, fileDigestModel.md5());
                }

            }
            return map;
        }
    }

    public static record DirCompareResult(Set<String> deleteFileStrSet, Set<String> editFileStrSet,
                                          Set<String> addFileStrSet) {

    }
}
