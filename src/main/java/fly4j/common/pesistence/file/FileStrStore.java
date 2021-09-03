package fly4j.common.pesistence.file;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * dataDir 数据存储的目录,比如路径为/export/data；下边可以存放不同数据，通过pin和innerDir来区分，可以理解为不通数据库
 *
 * @author qryc created:forget;created is before:2020/03/07
 * @author qryc 2020年3月21日 rename FileKv to FileStrStore
 * 有时候我们总是把简单的事情搞复杂，上面注释留作纪念吧，曾经设计的太复杂了，既然是文件系统，不用非仿作kv,直接改为文件系统，应用随便拼吧
 */
public class FileStrStore {
    private static Charset fileCharset = Charset.forName("utf-8");


    public static void setValue(Path storePath, String value) {
        try {
            if (Files.notExists(storePath.getParent())) {
                Files.createDirectories(storePath.getParent());
            }
            Files.writeString(storePath, value);
//            FileUtils.writeStringToFile(storePath.toFile(), value, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getValue(Path storePath) {
        return readFileToStr(storePath);
    }


    public static void delete(String storePath) {
        File file = new File(storePath);

        if (file.isFile() && file.exists())
            file.delete();
    }

    public static void delete(Path storePath) {
        File file = storePath.toFile();

        if (file.isFile() && file.exists())
            file.delete();
    }

    public static List<String> getValues(Path parentPath, String keyPre) {
        List<String> values = new ArrayList<>();
        Collection<File> files = listDirFiles(parentPath);
        for (File file : files) {
            if (file.getName().startsWith(".")) {
                continue;
            }
            if (file.getName().startsWith(keyPre)) {

                String json = readFileToStr(file);
                values.add(json);
            }
        }
        return values;
    }

    protected static String readFileToStr(File file) {
        if (!file.exists()) {
            return null;
        }
        try {
//            return FileUtils.readFileToString(file, fileCharset);
            return Files.readString(file.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<String> getChildFileNames(Path storePath, String keyPre) {
        List<String> values = new ArrayList<>();
        Collection<File> files = listDirFiles(storePath);
        for (File file : files) {
            if (file.getName().startsWith(".")) {
                continue;
            }
            if (file.getName().startsWith(keyPre)) {
                values.add(file.getName());
            }
        }
        return values;
    }

    private static Collection<File> listDirFiles(Path dirStr) {
        File file = dirStr.toFile();
        if (!file.isDirectory()) {
            return new ArrayList<File>();
        }

        Collection<File> files = FileUtils.listFiles(file, null, false);
        return files;
    }

    protected static String readFileToStr(Path filePath) {
        return readFileToStr(filePath.toFile());
    }


}
