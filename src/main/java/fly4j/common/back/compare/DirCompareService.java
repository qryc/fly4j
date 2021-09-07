package fly4j.common.back.compare;

import fly4j.common.back.version.DirMd5Calculate;
import fly4j.common.back.version.DirVersionCheckParam;
import fly4j.common.back.version.DirVersionModel;
import fly4j.common.lang.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class DirCompareService {
    //md5 or size


    public static FlyResult compareTwoDir(File histoyDir, File currentDir, DirVersionCheckParam checkParam) {
        try {
            final StringBuilder stringBuilder = new StringBuilder();
            FlyResult flyResult = new FlyResult().success();
            AtomicLong count = new AtomicLong(0);

            StringConst.appendLine(stringBuilder, "....current file " + currentDir.getAbsolutePath() + " compare to history:" + histoyDir.getAbsolutePath());
            //取得上次的md5
            Map<String, String> historyMd5MapRead = DirMd5Calculate.getDirMd5Map(histoyDir.getAbsolutePath(), checkParam);
            //取得文件夹的Md5
            Map<String, String> currentMd5Map = DirMd5Calculate.getDirMd5Map(currentDir.getAbsolutePath(), checkParam);

            return FileMapCompareUtil.compareTwoMap(stringBuilder, flyResult, count,
                    FileMapCompareUtil.trimPath(historyMd5MapRead),
                    FileMapCompareUtil.trimPath(currentMd5Map));
        } catch (Exception e) {
            e.printStackTrace();
            FlyResult flyResult = new FlyResult().success();
            final StringBuilder stringBuilder = new StringBuilder();
            StringConst.appendLine(stringBuilder, "Exception:" + e.getMessage());
            return flyResult.append(stringBuilder.toString());
        }

    }





}
