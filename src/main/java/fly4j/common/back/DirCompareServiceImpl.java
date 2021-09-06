package fly4j.common.back;

import fly4j.common.back.model.DirVersionModel;
import fly4j.common.file.DirMd5Calculate;
import fly4j.common.lang.*;
import fly4j.common.file.FileAndDirFilter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class DirCompareServiceImpl implements DirCompareService {
    //md5 or size
    private FileAndDirFilter noNeedCalMd5FileFilter;
    private boolean checkEmptyDir = false;

    public void setNoNeedCalMd5FileFilter(FileAndDirFilter noNeedCalMd5FileFilter) {
        this.noNeedCalMd5FileFilter = noNeedCalMd5FileFilter;
    }


    @Override
    public FlyResult compareMulDir(File histoyDir, File currentDir) {
        try {
            final StringBuilder stringBuilder = new StringBuilder();
            FlyResult flyResult = new FlyResult().success();
            AtomicLong count = new AtomicLong(0);

            StringConst.appendLine(stringBuilder, "....current file " + currentDir.getAbsolutePath() + " compare to history:" + histoyDir.getAbsolutePath());
            //取得上次的md5
            DirMd5Calculate.DirVersionCheckParam2 dirMd5ParamHistory = new DirMd5Calculate.DirVersionCheckParam2(histoyDir, VersionType.LEN,
                    checkEmptyDir, noNeedCalMd5FileFilter);
            Map<String, String> historyMd5MapRead = DirMd5Calculate.getDirMd5Map(dirMd5ParamHistory);
            //取得文件夹的Md5
            DirMd5Calculate.DirVersionCheckParam2 dirMd5ParamCurrent = new DirMd5Calculate.DirVersionCheckParam2(currentDir, VersionType.LEN,
                    checkEmptyDir, noNeedCalMd5FileFilter);
            Map<String, String> currentMd5Map = DirMd5Calculate.getDirMd5Map(dirMd5ParamCurrent);

            return compareTwoMap(stringBuilder, flyResult, count, trimPath(historyMd5MapRead), trimPath(currentMd5Map));
        } catch (Exception e) {
            e.printStackTrace();
            FlyResult flyResult = new FlyResult().success();
            final StringBuilder stringBuilder = new StringBuilder();
            StringConst.appendLine(stringBuilder, "Exception:" + e.getMessage());
            return flyResult.append(stringBuilder.toString());
        }

    }

    @Override
    public FlyResult checkDirChange(File checkDir, File md5File, VersionType versionType) {
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
            //取得上次的md5
            String historyMd5Str = FileUtils.readFileToString(md5File, Charset.forName("utf-8"));
            DirVersionModel dirVersionModel = JsonUtils.readValue(historyMd5Str, DirVersionModel.class);
            Map<String, String> historyMd5MapRead = dirVersionModel.getFilesMap(versionType);
            //取得文件夹的Md5
            DirMd5Calculate.DirVersionCheckParam2 dirMd5Param = new DirMd5Calculate.DirVersionCheckParam2(checkDir, versionType,
                    checkEmptyDir, noNeedCalMd5FileFilter);
            Map<String, String> currentMd5Map = DirMd5Calculate.getDirMd5Map(dirMd5Param);

            return compareTwoMap(stringBuilder, flyResult, count, trimPath(historyMd5MapRead), trimPath(currentMd5Map));
        } catch (Exception e) {
            e.printStackTrace();
            FlyResult flyResult = new FlyResult().success();
            final StringBuilder stringBuilder = new StringBuilder();
            StringConst.appendLine(stringBuilder, "Exception:" + e.getMessage());
            return flyResult.append(stringBuilder.toString());
        }

    }

    private Map<String, String> trimPath(Map<String, String> map) {
//        return map.entrySet().stream().collect(Collectors.toMap(key -> key, value -> value));
        Map<String, String> mapNew = new LinkedHashMap<>();
        map.forEach((key, value) -> {
            if (key.startsWith("/") || key.startsWith("\\")) {
                key = key.substring(1);
            }
            mapNew.put(key, value);
        });
        return mapNew;
    }

    /**
     * historyMd5MapRead 为基准
     */
    private FlyResult compareTwoMap(StringBuilder stringBuilder, FlyResult flyResult, AtomicLong count, Map<String, String> historyMd5MapRead, Map<String, String> currentMd5Map) {
        Set<String> deleteKeys = new HashSet<>();
        Set<String> addKeys = new HashSet<>();
        Map<String, String> moveKeys = new HashMap<>();
        Map<String, String> moveLikeKeys = new HashMap<>();
        Map<String, String> moveRealKeys = new HashMap<>();
        historyMd5MapRead.forEach((oldKey, oValue) -> {
            count.addAndGet(1);
            String md5New = currentMd5Map.get(oldKey);
            if (null == md5New) {
                flyResult.fail();
                deleteKeys.add(oldKey);
            } else if (md5New.equals(oValue)) {

            } else {
                flyResult.fail();
                StringConst.appendLine(stringBuilder, "........diff:" + oldKey);
            }
        });
        currentMd5Map.forEach((cKey, cValue) -> {
            String md5History = historyMd5MapRead.get(cKey);
            if (null == md5History) {
                addKeys.add(cKey);
            }
        });

        //寻找move
        deleteKeys.forEach(deleteKey -> {
            //遍历新的是否有新的
            addKeys.forEach(newKey -> {
                String newMd5 = currentMd5Map.get(newKey);
                boolean md5Equal = newMd5 != null && newMd5.equals(historyMd5MapRead.get(deleteKey));
                boolean fileNameEqual = FilenameUtils.getName(newKey).equals(FilenameUtils.getName(deleteKey));
                if (md5Equal && !"dir".equals(newMd5)) {
                    moveKeys.put(deleteKey, newKey);
                    if (fileNameEqual) {
                        moveRealKeys.put(deleteKey, newKey);
                    } else {
                        moveLikeKeys.put(deleteKey, newKey);
                    }
                }

            });

        });
        deleteKeys.forEach(deleteKey -> {
            if (!moveKeys.containsKey(deleteKey))
                StringConst.appendLine(stringBuilder, "........delete:" + deleteKey);
        });
        addKeys.forEach(addKey -> {
            if (!moveKeys.values().contains(addKey))
                StringConst.appendLine(stringBuilder, "........add:" + addKey);
        });
        moveLikeKeys.forEach((from, to) -> {
            StringConst.appendLine(stringBuilder, ".....movelike: " + from);
            StringConst.appendLine(stringBuilder, "...........to: " + to);
        });
        moveRealKeys.forEach((from, to) -> {
            StringConst.appendLine(stringBuilder, "........move: " + from);
            StringConst.appendLine(stringBuilder, "..........to: " + to);
        });

        StringConst.appendLine(stringBuilder, "end checkCount:" + count.get());
        return flyResult.append(stringBuilder.toString());
    }

    public void setCheckEmptyDir(boolean checkEmptyDir) {
        this.checkEmptyDir = checkEmptyDir;
    }

}
