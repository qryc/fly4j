package fly4j.common.back.compare;

import fly4j.common.back.version.DigestType;
import fly4j.common.back.version.DirDigestCalculate;
import fly4j.common.back.version.DirVersionCheckParam;
import fly4j.common.file.FileAndDirFilter;
import fly4j.common.file.FileUtil;
import fly4j.common.lang.*;
import fly4j.common.lang.map.MapCompareResult;
import fly4j.common.lang.map.MapUtil;

import java.io.File;
import java.util.*;

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

    /**
     * 技术化术语太难看懂了，使用备份场景化命名
     * 查找新文件夹中和老文件夹重复的文件，并删除
     *
     * @param readyDir
     * @param newDir
     * @return
     */
    public static FlyResult deleteNotNeedBack(Map<File, File> deleteFileMaps) {

        FlyResult flyResult = new FlyResult().success();
        deleteFileMaps.forEach((deleteFile, repeatFile) -> {
            FileUtil.deleteOneRepeatFile(deleteFile, repeatFile);
        });
        return flyResult;

    }

    public static Map<File, File> getDeleteDoubleFileMap(File readyDir, File newDir, FileAndDirFilter noNeedCalMd5FileFilter) {
        DirVersionCheckParam checkParam = new DirVersionCheckParam(DigestType.LEN, false, noNeedCalMd5FileFilter);
        //Ready的md5
        Map<File, String> readyLenMap_file = DirDigestCalculate.getDirDigestFileMap(readyDir.getAbsolutePath(), checkParam);
        //取得新文件夹的Md5
        Map<File, String> newLenMap_file = DirDigestCalculate.getDirDigestFileMap(newDir.getAbsolutePath(), checkParam);

        //查找新文件夹中和老的文件夹长度一致的文件
        List<File> readyDoubleFile = new ArrayList<>();
        List<File> newDoubleFile = new ArrayList<>();
        LinkedHashMap<String, List<File>> readyLengthGroupFileMap_len = MapUtil.convert2ValueMap(readyLenMap_file);
        newLenMap_file.forEach((file, lenStr) -> {
            if (readyLengthGroupFileMap_len.containsKey(lenStr)) {
                //长度相等的嫌疑统统加入
                readyDoubleFile.addAll(readyLengthGroupFileMap_len.get(lenStr));
                newDoubleFile.add(file);
            }
        });

        //进一步删选MD5一致的文件
        //先生成新的反向
        LinkedHashMap<String, List<File>> readyMd5RevertMap_md5 = DirDigestCalculate.getFilesMd5DoubleMap(readyDoubleFile);
        LinkedHashMap<String, List<File>> newMd5RevertMap_md5 = DirDigestCalculate.getFilesMd5DoubleMap(newDoubleFile);
        //遍历老的，如果新的有存在重复，则删除
        //key 要删除的，value，重复文件
        Map<File, File> deleteFileMaps = new LinkedHashMap<>();
        readyMd5RevertMap_md5.forEach((md5Str, files) -> {
            if (newMd5RevertMap_md5.containsKey(md5Str)) {
                //要删除的
                List<File> deleteFiles = newMd5RevertMap_md5.get(md5Str);
                deleteFiles.forEach(delFile -> {
                    deleteFileMaps.put(delFile, files.get(0));
                });
            }
        });
        return deleteFileMaps;
    }

}
