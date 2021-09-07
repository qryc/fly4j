package fly4j.common.back.compare;

import fly4j.common.back.version.DirDigestCalculate;
import fly4j.common.back.version.DirVersionCheckParam;
import fly4j.common.lang.*;
import fly4j.common.lang.map.MapCompareResult;
import fly4j.common.lang.map.MapUtil;
import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class DirCompareService {
    //md5 or size


    public static FlyResult compareTwoDir(File histoyDir, File currentDir, DirVersionCheckParam checkParam) {
        try {

            FlyResult flyResult = new FlyResult().success();
            flyResult.appendLine("....current file " + currentDir.getAbsolutePath() + " compare to history:" + histoyDir.getAbsolutePath());
            //取得上次的md5
            Map<String, String> historyMd5MapRead = DirDigestCalculate.getDirDigestMap(histoyDir.getAbsolutePath(), checkParam);
            //取得文件夹的Md5
            Map<String, String> currentMd5Map = DirDigestCalculate.getDirDigestMap(currentDir.getAbsolutePath(), checkParam);
            MapCompareResult mapCompareResult = MapUtil.compareTwoMap(historyMd5MapRead, currentMd5Map);
            return flyResult.merge(mapCompareResult.toFlyResult());
        } catch (Exception e) {
            e.printStackTrace();
            return new FlyResult().fail("Exception:" + e.getMessage());
        }

    }


}
