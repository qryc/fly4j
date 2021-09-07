package fly4j.common.back.compare;

import fly4j.common.file.FilenameUtil;
import fly4j.common.lang.FlyResult;
import fly4j.common.lang.StringConst;
import org.apache.commons.io.FilenameUtils;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by qryc on 2021/9/7
 */
public class FileMapCompareUtil {
    /**
     * historyMd5MapRead 为基准
     */
    public static FlyResult compareTwoMap(Map<String, String> historyMd5MapRead, Map<String, String> currentMd5Map) {
        StringBuilder stringBuilder=new StringBuilder();
        AtomicLong count = new AtomicLong(0);
        FlyResult flyResult = new FlyResult().success();
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



}
