package fly4j.common.back;

import fly4j.common.back.param.DirVersionCheckParam;
import fly4j.common.lang.FlyResult;

import java.io.File;

public interface DirCompareService {


    FlyResult checkDirChange(File checkDir, File md5File, DirVersionCheckParam checkParam);


    FlyResult compareTwoDir(File histoyDir, File currentDir, DirVersionCheckParam checkParam);


}
