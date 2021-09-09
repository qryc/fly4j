package fly4j.common.back.version;

import fly4j.common.file.FileUtil;
import fly4j.common.file.FilenameUtil;
import fly4j.common.lang.map.LinkedHashMapUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class DirDigestCalculate {
    public static boolean ignoreMacShadowFile = true;
    public static final String DIR_VALUE = "dir";

    public static LinkedHashMap<String, List<File>> getFilesMd5DoubleMap(List<File> doubleFilesByLen) {
        LinkedHashMap<String, List<File>> md5RevertMap_md5 = new LinkedHashMap<>();
        doubleFilesByLen.forEach(file -> {
            String md5 = FileUtil.getMD5(file);
            List<File> files = md5RevertMap_md5.computeIfAbsent(md5, key -> new ArrayList<>());
            files.add(file);
        });
        return md5RevertMap_md5;
    }

    public static LinkedHashMap<String, String> getDirDigestMap(String checkBaseDirStr, BackModel.DirVersionCheckParam checkParam) {
        LinkedHashMap<File, String> digestFileMap = getDirDigestFileMap(checkBaseDirStr, checkParam);
        return LinkedHashMapUtil.alterKey(digestFileMap, file -> FilenameUtil.getSubPathUnix(file.getAbsolutePath(), checkBaseDirStr));
    }

    public static LinkedHashMap<File, String> getDirDigestFileMap(String checkBaseDirStr, BackModel.DirVersionCheckParam checkParam) {
        return getDirDigestFileMap(new File(checkBaseDirStr), checkParam);
    }

    public static LinkedHashMap<File, String> getDirDigestFileMap(File checkBaseDir, BackModel.DirVersionCheckParam checkParam) {
        LinkedHashMap<File, String> md5FileMap = new LinkedHashMap<>();
        AtomicLong count = new AtomicLong(0);
        try {
            Files.walk(checkBaseDir.toPath()).forEach(path -> {
                File file = path.toFile();
                if (null != checkParam.noNeedCalMd5FileFilter() && checkParam.noNeedCalMd5FileFilter().accept(file)) {
                    return;
                }
                //如果不是空文件夹，把父亲文件夹加入
                if (file.isDirectory()) {
                    if (checkParam.checkDirFlag()) {
                        md5FileMap.put(file, DIR_VALUE);
                    }
                } else {
                    //生成md5
                    count.incrementAndGet();
                    System.out.println("check file " + count + " :" + file.getAbsolutePath());

                    if (ignoreMacShadowFile) {
                        if (!file.getAbsolutePath().contains("._")) {
                            md5FileMap.put(file, getMd5(file, checkParam.digestType()));
                        }
                    } else {
                        md5FileMap.put(file, getMd5(file, checkParam.digestType()));
                    }
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return md5FileMap;
    }


    public static String getMd5(File file, BackModel.DigestType versionType) {
        return BackModel.DigestType.LEN.equals(versionType) ? "" + file.length() : FileUtil.getMD5(file);
    }


}
