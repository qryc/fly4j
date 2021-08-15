package fly4j.common.back;

import fly4j.common.lang.FlyResult;

import java.io.File;
import java.util.List;
import java.util.Map;

public interface DirCompareService {
    Map<String, String> getDirMd5Map(File checkDir, VersionType versionType);

    String genDirMd5VersionTag(File beZipSourceDir, File sourceMd5File, VersionType versionType);


    FlyResult checkDirChange(File checkDir, File md5File, VersionType versionType);

    FlyResult compareMulDir(List<File> compDirs, VersionType versionType);

}
