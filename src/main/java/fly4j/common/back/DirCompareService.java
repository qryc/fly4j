package fly4j.common.back;

import fly4j.common.lang.FlyResult;

import java.io.File;
import java.util.List;
import java.util.Map;

public interface DirCompareService {
    Map<String, String> getDirMd5Map(File checkDir);

    void genDirMd5VersionTag(File beZipSourceDir, File sourceMd5File);


    FlyResult checkDirChange(File checkDir, File md5File);

    FlyResult compareMulDir(List<File> compDirs);

}
