package fly4j.common.lang.map;

import fly4j.common.lang.FlyResult;
import fly4j.common.lang.StringConst;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by qryc on 2021/9/7
 */
public class MapCompareResult {
    public AtomicLong count = new AtomicLong(0);
    public Set<String> deleteKeys = new HashSet<>();
    public Set<String> addKeys = new HashSet<>();
    public Set<String> diffKeys = new HashSet<>();

    /**
     * historyMd5MapRead 为基准
     */
    public FlyResult toFlyResult() {
        StringBuilder stringBuilder = new StringBuilder();
        AtomicLong count = new AtomicLong(0);
        FlyResult flyResult = new FlyResult().success();
        if (deleteKeys.size() > 0 || addKeys.size() > 0 || diffKeys.size() > 0) {
            flyResult.fail();
        }
        //寻找move
        diffKeys.forEach(key -> {
            stringBuilder.append("........diff:" + key).append(StringConst.LF);
        });
        deleteKeys.forEach(key -> {
            stringBuilder.append("........delete:" + key).append(StringConst.LF);
        });
        addKeys.forEach(addKey -> {
            stringBuilder.append("........add:" + addKey).append(StringConst.LF);
        });

        stringBuilder.append("end checkCount:" + count.get()).append(StringConst.LF);
        return flyResult.append(stringBuilder.toString());
    }

}
