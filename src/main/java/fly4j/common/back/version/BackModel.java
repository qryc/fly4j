package fly4j.common.back.version;

import fly4j.common.file.FileAndDirFilter;
import fly4j.common.file.FilenameUtil;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by qryc on 2021/9/3
 */
public class BackModel {
    public enum DigestType {
        MD5, LEN
    }
    public static record DirVersionCheckParam(DigestType digestType, boolean checkDirFlag,
                                              FileAndDirFilter noNeedCalMd5FileFilter) {
    }

    public static record DirVersionGenParam(String checkBaseDirStr, FileAndDirFilter noNeedCalMd5FileFilter,
                                            String checkDate) {
    }


    public static record FileDigestModel(String pathStr, Long length, String md5) {
    }

    /**
     * Created by qryc on 2021/9/3
     */
    public static record DirDigestModel(String path, Long length) {
    }

    /**
     * 整个文件夹信息摘要，包含里面的文件与文件夹
     * Created by qryc on 2021/9/3
     */
    public static record DirDigestAllModel(Map<String, String> environment,
                                           DirVersionGenParam checkParam,
                                           List<FileDigestModel> fileDigestModels,
                                           List<DirDigestModel> dirDigestModels) {

        public Map<String, String> getFilesDigestMap(DigestType versionType) {
            Map<String, String> map = new LinkedHashMap<>();
            for (FileDigestModel fileDigestModel : fileDigestModels) {
                var dirKey = FilenameUtil.getSubPathUnix(fileDigestModel.pathStr(), checkParam.checkBaseDirStr());
                if (DigestType.LEN.equals(versionType)) {
                    map.put(dirKey, "" + fileDigestModel.length());
                } else {
                    map.put(dirKey, fileDigestModel.md5());
                }

            }
            return map;
        }
    }
}
