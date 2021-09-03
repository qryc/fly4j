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
    Map<String, String> getDirMd5Map(File checkDir, VersionType versionType);

    String genDirMd5VersionTag(File beZipSourceDir, Path md5StorePath, VersionType versionType);


    FlyResult checkDirChange(File checkDir, File md5File, VersionType versionType);


    FlyResult compareMulDir(List<File> compDirs, VersionType versionType);

    static String getDefaultVersionFileName(String beZipSourceDir) {
        if (null == beZipSourceDir) {
            beZipSourceDir = "";
        }
        return FlyString.getPlanText(beZipSourceDir) + DateUtil.getHourStr4Name(new Date())  + ".md5";
    }

}
