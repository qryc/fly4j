package fly4j.common.file.store;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Created by qryc on 2020/5/3.
 */
public class TestFileStrStore {
    public void demo() throws IOException {
        FileJsonStrStore.saveObject(Path.of("d:\\flyConfig.json"), new Object());
    }
}
