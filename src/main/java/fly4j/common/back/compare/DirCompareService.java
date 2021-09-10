package fly4j.common.back.compare;

import fly4j.common.back.version.BackModel;
import fly4j.common.back.version.DirDigestCalculate;
import fly4j.common.file.FileAndDirFilter;
import fly4j.common.lang.FlyResult;
import fly4j.common.lang.map.MapCompareResult;
import fly4j.common.lang.map.MapUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DirCompareService {
    //md5 or size
    public static FlyResult compareTwoDir(File histoyDir, File currentDir, BackModel.DirVersionCheckParam checkParam) {
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
     * @param standardDir   对比标准文件件
     * @param doubleKillDir
     * @return
     */
    public static Map<File, File> getDeleteDoubleFileMap(File standardDir, File doubleKillDir, FileAndDirFilter noNeedCalMd5FileFilter) {
        /**第一步：通过长度进行第一轮筛选长度一致的可疑文件**/
        BackModel.DirVersionCheckParam checkParam = new BackModel.DirVersionCheckParam(BackModel.DigestType.LEN, false, noNeedCalMd5FileFilter);
        //Ready的md5
        Map<File, String> readyLenMap_file = DirDigestCalculate.getDirDigestFileMap(standardDir, checkParam);
        //取得新文件夹的Md5
        Map<File, String> newLenMap_file = DirDigestCalculate.getDirDigestFileMap(doubleKillDir, checkParam);

        //查找新文件夹中和老的文件夹长度一致的文件
        List<File> standardDoubleFilesFromLen = new ArrayList<>();
        List<File> doubleKillDoubleFileFromLen = new ArrayList<>();
        LinkedHashMap<String, List<File>> readyLengthGroupFileMap_len = MapUtil.convert2ValueMap(readyLenMap_file);
        newLenMap_file.forEach((file, lenStr) -> {
            if (readyLengthGroupFileMap_len.containsKey(lenStr)) {
                //长度相等的嫌疑统统加入
                standardDoubleFilesFromLen.addAll(readyLengthGroupFileMap_len.get(lenStr));
                doubleKillDoubleFileFromLen.add(file);
            }
        });

        //进一步删选MD5一致的文件
        //先生成新的反向
        LinkedHashMap<String, List<File>> standardMd5RevertMap_md5 = DirDigestCalculate.getFilesMd5DoubleMap(standardDoubleFilesFromLen);
        LinkedHashMap<String, List<File>> doubleKillMd5RevertMap_md5 = DirDigestCalculate.getFilesMd5DoubleMap(doubleKillDoubleFileFromLen);
        //遍历老的，如果新的有存在重复，则删除
        //key 要删除的，value，重复文件
        Map<File, File> deleteFileMaps = new LinkedHashMap<>();
        standardMd5RevertMap_md5.forEach((md5Str, stardardFiles) -> {
            if (doubleKillMd5RevertMap_md5.containsKey(md5Str)) {
                //要删除的
                doubleKillMd5RevertMap_md5.get(md5Str).forEach(doubleKillFile -> {
                    deleteFileMaps.put(doubleKillFile, stardardFiles.get(0));
                });
            }
        });
        return deleteFileMaps;
    }

}
