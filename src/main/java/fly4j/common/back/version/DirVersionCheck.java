package fly4j.common.back.version;

import fly4j.common.back.compare.FileMapCompareUtil;
import fly4j.common.lang.DateUtil;
import fly4j.common.lang.FlyResult;
import fly4j.common.lang.JsonUtils;
import fly4j.common.lang.StringConst;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by qryc on 2021/9/7
 */
public class DirVersionCheck {

    public static FlyResult checkDirChange(File checkDir, File md5File, DirVersionCheckParam checkParam) {
        try {
            final StringBuilder stringBuilder = new StringBuilder();
            StringConst.appendLine(stringBuilder, "checkDir:" + checkDir);
            if (null != md5File)
                StringConst.appendLine(stringBuilder, "md5:" + md5File.getAbsolutePath());
            if (null == md5File) {
                stringBuilder.append(" not have history file");
                return new FlyResult(true, stringBuilder.toString());
            }
            FlyResult flyResult = new FlyResult().success();
            AtomicLong count = new AtomicLong(0);

            StringConst.appendLine(stringBuilder, "....current file compare to history:" + DateUtil.getDateStr(new Date(md5File.lastModified())));
            //取得上次的文件夹digest摘要信息
            String historyJsonStr = FileUtils.readFileToString(md5File, Charset.forName("utf-8"));
            DirDigestAllModel dirDigestAllModel = JsonUtils.readValue(historyJsonStr, DirDigestAllModel.class);
            Map<String, String> historyMd5MapRead = dirDigestAllModel.getFilesDigestMap(checkParam.digestType());
            //取得文件夹的当前的digest信息
            Map<String, String> currentMd5Map = DirDigestCalculate.getDirDigestMap(checkDir.getAbsolutePath(), checkParam);

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
