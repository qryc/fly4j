package fly4j.common.back;

import fly4j.common.lang.DateUtil;
import fly4j.common.lang.FlyResult;
import fly4j.common.lang.FlyString;
import fly4j.common.pesistence.file.FileJsonStrStore;

import java.io.File;
import java.nio.file.Path;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface DirCompareService {


    FlyResult checkDirChange(File checkDir, File md5File, VersionType versionType);


    FlyResult compareMulDir(File histoyDir,File currentDir);



}
