package fly4j.common.pesistence.file;

import java.nio.file.Path;

/**
 * Created by qryc on 2020/5/3.
 */
public class TestFileStrStore {
    public void demo() {
        FileJsonStrStore.saveObject(Path.of("d:\\flyConfig.json"), new Object());
    }
}
