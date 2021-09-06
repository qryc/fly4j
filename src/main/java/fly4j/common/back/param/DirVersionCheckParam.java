package fly4j.common.back.param;

import fly4j.common.back.VersionType;
import fly4j.common.file.FileAndDirFilter;

/**
 * Created by qryc on 2021/9/6
 */
public record DirVersionCheckParam(VersionType versionType, boolean checkDirFlag,
                                   FileAndDirFilter noNeedCalMd5FileFilter) {
}