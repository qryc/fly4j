package flynote.applicaion.service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import farticle.domain.entity.DtreeObj;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class TreeCache {
    private static Cache<String, List<DtreeObj>> treeCache = CacheBuilder
            .newBuilder().maximumSize(100).expireAfterWrite(10, TimeUnit.MINUTES)
            .build();

    public static List<DtreeObj> getDtreeObjs(String pin) {
        String treeKey = pin + "dtreeObjs";
        return TreeCache.treeCache.getIfPresent(treeKey);

    }

    public static void putDtreeObjs(String pin, List<DtreeObj> dtreeObjs) {
        String treeKey = pin + "dtreeObjs";
        treeCache.put(treeKey, dtreeObjs);

    }

    public static void clear(String pin) {
        String treeKey = pin + "dtreeObjs";
        treeCache.invalidate(treeKey);

    }
}
