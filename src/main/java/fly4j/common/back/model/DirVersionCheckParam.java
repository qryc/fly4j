package fly4j.common.back.model;

import fly4j.common.file.FileAndDirFilter;

/**
 * Created by qryc on 2021/9/3
 */
public record DirVersionCheckParam(String checkBaseDirStr, FileAndDirFilter noNeedCalMd5FileFilter, String checkDate) {

}
