package fly4j.common.back.version;

import fly4j.common.file.FileAndDirFilter;
import fly4j.common.lang.DateUtil;
import fly4j.common.lang.FlyResult;
import fly4j.common.lang.JsonUtils;
import fly4j.common.lang.map.MapUtil;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;

/**
 * Created by qryc on 2021/9/7
 */
public class DirVersionCheck {

    public static BackModel.DirCompareResult checkDirChange(File checkDir, File md5File, FileAndDirFilter noNeedCalMd5FileFilter) throws IOException {

        if (null != md5File)
            throw new RuntimeException("md5:" + md5File.getAbsolutePath());

        if (null == md5File) {
            throw new RuntimeException(" not have history file");
        }

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
        Set<String> deleteOrEditFileStrSet = new LinkedHashSet<>();
        standardMd5RevertMap_md5.forEach((md5Str, fileStrs) -> {
            if (!currentMd5RevertMap_md5.containsKey(md5Str)) {
                deleteOrEditFileStrSet.addAll(fileStrs);
            }
        });

        //修改或新增的
        Set<String> deleteFileStrSet = new LinkedHashSet<>();
        Set<String> editFileStrSet = new LinkedHashSet<>();
        Set<String> addFileStrSet = new LinkedHashSet<>();
        currentMd5RevertMap_md5.forEach((md5Str, fileStrs) -> {
            if (!standardMd5RevertMap_md5.containsKey(md5Str)) {

                fileStrs.forEach(fileStr -> {
                    if (deleteOrEditFileStrSet.contains(fileStr)) {
                        //修改文件：在前面删除中存在的文件
                        editFileStrSet.add(fileStr);
                    } else {
                        //找出新增的文件
                        addFileStrSet.add(fileStr);
                    }
                });

            }
        });
        deleteOrEditFileStrSet.forEach(fileStr -> {
            if (!editFileStrSet.contains(fileStr)) {
                deleteFileStrSet.add(fileStr);
            }
        });
        return new BackModel.DirCompareResult(deleteFileStrSet, editFileStrSet, addFileStrSet);

    }
}
