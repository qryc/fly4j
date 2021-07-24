package fly4j.common.file;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class DirMd5Calculate {
    public static Map<String, String> getDirMd5Map(DirMd5Param dirMd5Param) {
        Map<String, String> md5Map = new LinkedHashMap<>();
        DirMd5OutputParam innerParam = new DirMd5OutputParam(md5Map, new AtomicLong(0));
        DirMd5Calculate.getMd5FileStr(dirMd5Param.checkBaseDir, innerParam, dirMd5Param);
        return md5Map;
    }

    private static void getMd5FileStr(File dirFile, DirMd5OutputParam outputParam, DirMd5Param dirMd5Param) {
        File baseDir = dirMd5Param.checkBaseDir;
        try {
            File[] files = dirFile.listFiles();
            var dirKey = FileUtil.getSubPathUnix(dirFile.toPath(), baseDir.toPath());
            //如果不是空文件夹，把父亲文件夹加入
            if (dirMd5Param.checkEmptyDir) {
                outputParam.md5Map.put(dirKey, "dir");
            } else {
                if (files.length > 0) {
                    outputParam.md5Map.put(dirKey, "dir");
                }
            }

            for (File cfile : files) {

                if (null != dirMd5Param.noNeedCalMd5FileFilter && dirMd5Param.noNeedCalMd5FileFilter.accept(cfile)) {
                    continue;
                }
                if (cfile.isDirectory()) {
                    //递归
                    getMd5FileStr(cfile, outputParam, dirMd5Param);
                } else {
                    //生成md5

                    Long count = outputParam.count.incrementAndGet();
                    System.out.println("check file " + count + " :" + cfile.getAbsolutePath());

                    String key = FileUtil.getSubPathUnix(cfile.toPath(), baseDir.toPath());
                    if (DirMd5Calculate.DirMd5Param.CHECK_SIZE.equals(dirMd5Param.genType)) {
                        outputParam.md5Map.put(key, "" + cfile.length());
                    } else {
                        outputParam.md5Map.put(key, FileUtil.getMD5(cfile));
                    }

                }

            }
        } catch (Exception e) {
            System.out.println("dirFile:" + dirFile);
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    public static class DirMd5Param {
        public static final String CHECK_SIZE = "size";
        public static final String CHECK_MD5 = "md5";
        private File checkBaseDir;
        private String genType = CHECK_SIZE;
        private FileAndDirFilter noNeedCalMd5FileFilter;
        private boolean checkEmptyDir = true;

        public File getCheckBaseDir() {
            return checkBaseDir;
        }

        public void setCheckBaseDir(File checkBaseDir) {
            this.checkBaseDir = checkBaseDir;
        }

        public String getGenType() {
            return genType;
        }

        public void setGenType(String genType) {
            this.genType = genType;
        }

        public FileAndDirFilter getNoNeedCalMd5FileFilter() {
            return noNeedCalMd5FileFilter;
        }

        public void setNoNeedCalMd5FileFilter(FileAndDirFilter noNeedCalMd5FileFilter) {
            this.noNeedCalMd5FileFilter = noNeedCalMd5FileFilter;
        }

        public boolean isCheckEmptyDir() {
            return checkEmptyDir;
        }

        public void setCheckEmptyDir(boolean checkEmptyDir) {
            this.checkEmptyDir = checkEmptyDir;
        }
    }

    private static record DirMd5OutputParam(Map<String, String> md5Map, AtomicLong count) {

    }
}
