package fly4j.common.back.version;

import fly4j.common.file.FileUtil;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 整个文件夹信息摘要，包含里面的文件与文件夹
 * Created by qryc on 2021/9/3
 */
public record DirDigestAllModel(Map<String, String> environment,
                                DirVersionGenParam checkParam,
                                List<FileDigestModel> fileDigestModels,
                                List<DirDigestModel> dirDigestModels) {

    public Map<String, String> getFilesDigestMap(DigestType versionType) {
        Map<String, String> map = new LinkedHashMap<>();
        for (FileDigestModel fileDigestModel : fileDigestModels) {
            var dirKey = FileUtil.getSubPathUnix(fileDigestModel.pathStr(), checkParam.checkBaseDirStr());
            if (DigestType.LEN.equals(versionType)) {
                map.put(dirKey, "" + fileDigestModel.length());
            } else {
                map.put(dirKey, fileDigestModel.md5());
            }

        }
        return map;
    }
}


