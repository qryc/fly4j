package fly4j.common.back.version;

import fly4j.common.file.FileAndDirFilter;
import fly4j.common.lang.DateUtil;
import fly4j.common.lang.FlyResult;
import fly4j.common.lang.JsonUtils;
import fly4j.common.lang.map.MapCompareResult;
import fly4j.common.lang.map.MapUtil;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by qryc on 2021/9/7
 */
public class DirVersionCheck {

    public static FlyResult checkDirChange(File checkDir, File md5File, FileAndDirFilter noNeedCalMd5FileFilter) {
        try {
            FlyResult flyResult = new FlyResult().success();
            flyResult.appendLine("checkDir:" + checkDir);

            if (null != md5File)
                flyResult.appendLine("md5:" + md5File.getAbsolutePath());

            if (null == md5File) {
                return flyResult.fail(" not have history file");
            }

            flyResult.appendLine("....current file compare to history:" + DateUtil.getDateStr(new Date(md5File.lastModified())));
            //取得上次的文件夹digest摘要信息
            String historyJsonStr = FileUtils.readFileToString(md5File, Charset.forName("utf-8"));
            BackModel.DirDigestAllModel dirDigestAllModel = JsonUtils.readValue(historyJsonStr, BackModel.DirDigestAllModel.class);
            Map<String, String> historyMd5MapRead = dirDigestAllModel.getFilesDigestMap(BackModel.DigestType.MD5);
            //取得文件夹的当前的digest信息
            Map<String, String> currentMd5Map = DirDigestCalculate.getDirDigestMap(checkDir.getAbsolutePath(), new BackModel.DirVersionCheckParam(BackModel.DigestType.MD5, false, noNeedCalMd5FileFilter));

            //进一步删选MD5一致的文件
            //先生成新的反向
            LinkedHashMap<String, List<String>> standardMd5RevertMap_md5 = MapUtil.convert2ValueMap(historyMd5MapRead);
            LinkedHashMap<String, List<String>> currentMd5RevertMap_md5 = MapUtil.convert2ValueMap(currentMd5Map);

            //查找删除的
            standardMd5RevertMap_md5.forEach((md5Str, stardardFiles) -> {
                if (!currentMd5RevertMap_md5.containsKey(md5Str)) {
                    //删除的
                    flyResult.fail();
                    flyResult.appendLine("deleteOrEdit:" + stardardFiles);
                }
            });

            //修改或新增的
            currentMd5RevertMap_md5.forEach((md5Str, stardardFiles) -> {
                if (!standardMd5RevertMap_md5.containsKey(md5Str)) {
                    //删除的
                    flyResult.fail();
                    flyResult.appendLine("addOrEdit:" + stardardFiles);
                }
            });
            return flyResult;
        } catch (Exception e) {
            e.printStackTrace();
            return new FlyResult().fail("Exception:" + e.getMessage());
        }

    }
}
