package flynote.infrastructure.impl;

import fly4j.common.test.util.TData;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import java.io.IOException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;


/**
 * Created by qryc on 2021/10/22
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class TestFile {


    @Before
    public void setup() throws Exception {
        TData.createTestDefaultFiles();
    }

    @Test
    public void deleteIfExists() throws Exception {

        Path pathA = TData.tPath.resolve("a.txt");
        Files.writeString(pathA, "aaa");
        Assert.assertTrue(Files.exists(pathA));
        try {
            Files.deleteIfExists(pathA.getParent());
            Assert.fail();
        } catch (DirectoryNotEmptyException e) {
            Assert.assertTrue(Files.exists(pathA));
        }

    }

    @Test
    public void renameTo() throws Exception {

        Path pathA = TData.tPath.resolve("a.txt");
        Assert.assertFalse(Files.exists(pathA));
        Files.writeString(pathA, "abc");
        Assert.assertTrue(Files.exists(pathA));
        Path pathB = TData.tPath.resolve("b.txt");
        Assert.assertFalse(Files.exists(pathB));
        pathA.toFile().renameTo(pathB.toFile());
        Assert.assertTrue(Files.exists(pathB));
        Assert.assertFalse(Files.exists(pathA));

    }

    @Test
    public void renameToExists() throws Exception {

        Path pathA = TData.tPath.resolve("a.txt");
        Assert.assertFalse(Files.exists(pathA));
        Files.writeString(pathA, "aaa");
        Assert.assertTrue(Files.exists(pathA));
        Path pathB = TData.tPath.resolve("b.txt");
        Files.writeString(pathB, "bbb");
        Assert.assertTrue(Files.exists(pathB));
        pathA.toFile().renameTo(pathB.toFile());
        Assert.assertTrue(Files.exists(pathB));
        Assert.assertFalse(Files.exists(pathA));
        Assert.assertEquals("aaa", Files.readString(pathB));

    }

    @Test
    public void move() throws IOException {

        Path pathA = TData.tPath.resolve("a.txt");
        Assert.assertFalse(Files.exists(pathA));
        Files.writeString(pathA, "abc");
        Assert.assertTrue(Files.exists(pathA));
        Path pathB = TData.tPath.resolve("b.txt");
        Assert.assertFalse(Files.exists(pathB));
        Files.move(pathA, pathB);
        Assert.assertTrue(Files.exists(pathB));
        Assert.assertFalse(Files.exists(pathA));

    }

    @Test
    public void moveExists() throws IOException {

        Path pathA = TData.tPath.resolve("a.txt");
        Assert.assertFalse(Files.exists(pathA));
        Files.writeString(pathA, "aaa");
        Assert.assertTrue(Files.exists(pathA));
        Path pathB = TData.tPath.resolve("b.txt");
        Files.writeString(pathB, "bbb");
        Assert.assertTrue(Files.exists(pathB));
        try {
            Files.move(pathA, pathB);
            Assert.fail();
        } catch (FileAlreadyExistsException e) {
            Assert.assertTrue(Files.exists(pathB));
            Assert.assertTrue(Files.exists(pathA));
        }


    }

    @After
    public void tearDown() throws Exception {
        FileUtils.forceDelete(TData.tPath.toFile());
    }


}
