package fly4j.common.pesistence.file;

import fly4j.common.lang.JsonUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author qryc 2021年7月13日
 */
public class FileJsonStrStore {
    public static void saveObject(Path storePath, Object value) throws IOException {
        Files.writeString(storePath, JsonUtils.writeValueAsString(value));
    }

    public static <T> T getObject(Path storePath, Class<T> cls) throws IOException {
        String json = Files.readString(storePath);
        return JsonUtils.readValue(json, cls);
    }
}
