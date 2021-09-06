package fly4j.common.back.model;

import fly4j.common.file.FileUtil;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by qryc on 2021/9/3
 */
public record DirVersionModel(Map<String, String> environment,
                              DirVersionGenParam checkParam,
                              List<SingleFileVersionModel> singleFileVersionModelList,
                              List<SingleDirVersionModel> singleDirVersionModels) {

    public Map<String, String> getFilesMap( VersionType versionType) {
        Map<String, String> map = new LinkedHashMap<>();
        for (SingleFileVersionModel singleFileVersionModel : singleFileVersionModelList) {
            var dirKey = FileUtil.getSubPathUnix(singleFileVersionModel.pathStr(),checkParam.checkBaseDirStr());
            if(VersionType.LEN.equals(versionType)){
                map.put(dirKey, ""+singleFileVersionModel.length());
            }else {
                map.put(dirKey, singleFileVersionModel.md5());
            }

        }
        return map;
    }
}


