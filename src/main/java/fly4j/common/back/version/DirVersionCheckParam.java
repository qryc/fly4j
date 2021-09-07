package fly4j.common.back.version;

import fly4j.common.file.FileAndDirFilter;

/**
 * Created by qryc on 2021/9/6
 */
public record DirVersionCheckParam(DigestType digestType, boolean checkDirFlag,
                                   FileAndDirFilter noNeedCalMd5FileFilter) {
}
