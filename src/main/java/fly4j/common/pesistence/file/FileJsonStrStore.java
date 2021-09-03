package fly4j.common.pesistence.file;

import fly4j.common.lang.JsonUtils;

import java.nio.file.Path;

/**
 * @author qryc 2021年7月13日
 */
public class FileJsonStrStore {
    public static void saveObject(Path storePath, Object value) {
        FileStrStore.setValue(storePath, JsonUtils.writeValueAsString(value));
    }

    public static <T> T getObject(Path storePath, Class<T> cls) {
        String json = FileStrStore.getValue(storePath);
        return JsonUtils.readValue(json, cls);
    }
}
