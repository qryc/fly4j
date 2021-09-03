package fly4j.common.file;

import fly4j.common.back.model.DirVersionCheckParam;
import fly4j.common.back.model.DirVersionModel;
import fly4j.common.back.model.SingleDirVersionModel;
import fly4j.common.back.model.SingleFileVersionModel;
import fly4j.common.back.VersionType;
import fly4j.common.lang.DateUtil;
import fly4j.common.os.OsUtil;
import fly4j.common.pesistence.file.FileJsonStrStore;

import java.io.File;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class DirMd5Calculate {
    public static boolean ignoreMacShadowFile = true;
    public static final String DIR_VALUE = "dir";

    public static LinkedHashMap<File, String> getDirMd5FileMap(File checkDir, VersionType versionType) {
        DirMd5Calculate.DirMd5Param dirMd5Param = new DirMd5Calculate.DirMd5Param();
        dirMd5Param.setCheckBaseDir(checkDir);
        dirMd5Param.setCheckDir(false);
        dirMd5Param.setGenType(versionType);
        dirMd5Param.setNoNeedCalMd5FileFilter(null);
        return DirMd5Calculate.getDirMd5FileMap(dirMd5Param);
    }

    public static LinkedHashMap<String, String> getDirMd5Map(DirMd5Param dirMd5Param) {

        LinkedHashMap<File, String> md5FileMap = getDirMd5FileMap(dirMd5Param);

        LinkedHashMap<String, String> md5Map = new LinkedHashMap<>();
        md5FileMap.forEach((file, str) -> {
            var dirKey = FileUtil.getSubPathUnix(file.toPath(), dirMd5Param.checkBaseDir.toPath());
            md5Map.put(dirKey, str);
        });
        return md5Map;
    }

    public static LinkedHashMap<File, String> getDirMd5FileMap(DirMd5Param dirMd5Param) {
        LinkedHashMap<File, String> md5FileMap = new LinkedHashMap<>();
        DirMd5OutputParam innerParam = new DirMd5OutputParam(md5FileMap, new AtomicLong(0));

        DirMd5Calculate.getMd5FileStr(dirMd5Param.checkBaseDir, innerParam, dirMd5Param);
        return md5FileMap;
    }

    public static String saveDirVersionModel2File(String checkDirStr, FileAndDirFilter noNeedCalMd5FileFilter, Path md5StorePath) {
        DirVersionCheckParam checkParam = new DirVersionCheckParam(checkDirStr,
                noNeedCalMd5FileFilter,
                DateUtil.getDateStr(new Date()));
        DirVersionModel dirVersionModel = DirMd5Calculate.genDirVersionModel(checkParam);
        FileJsonStrStore.saveObject(md5StorePath, dirVersionModel);
        System.out.println("save to file:" + md5StorePath);
        return md5StorePath.toString();
    }

    public static DirVersionModel genDirVersionModel(DirVersionCheckParam checkParam) {
        //文件版本信息
        List<SingleFileVersionModel> modelList = new ArrayList<>();
        List<SingleDirVersionModel> dirVersionModelList = new ArrayList<>();
        DirMd5OutputParam2 innerParam = new DirMd5OutputParam2(modelList, dirVersionModelList, new AtomicLong(0));
        DirMd5Calculate.getMd5FileStr2(new File(checkParam.checkBaseDirStr()), innerParam, checkParam);

        //环境信息
        Map<String, String> environment = new HashMap<>();
        environment.put("os.name", OsUtil.getOsName());

        return new DirVersionModel(environment, checkParam, modelList, dirVersionModelList);
    }

    private static void getMd5FileStr2(File dirFile, DirMd5OutputParam2 outputParam, DirVersionCheckParam checkParam) {
//        File baseDir = dirMd5Param.checkBaseDirStr;
        try {
            File[] files = dirFile.listFiles();
//            var dirKey = FileUtil.getSubPathUnix(dirFile.toPath(), baseDir.toPath());
            //如果不是空文件夹，把父亲文件夹加入

            outputParam.dirVersionModelList.add(new SingleDirVersionModel(dirFile.getAbsolutePath(),
                    Long.valueOf(files.length)));


            for (File cfile : files) {

                if (null != checkParam.noNeedCalMd5FileFilter() && checkParam.noNeedCalMd5FileFilter().accept(cfile)) {
                    continue;
                }
                if (cfile.isDirectory()) {
                    //递归
                    getMd5FileStr2(cfile, outputParam, checkParam);
                } else {
                    //生成md5

                    Long count = outputParam.count.incrementAndGet();
                    System.out.println("check file " + count + " :" + cfile.getAbsolutePath());

//                    String key = FileUtil.getSubPathUnix(cfile.toPath(), baseDir.toPath());
                    if (ignoreMacShadowFile) {
                        if (!cfile.getAbsolutePath().contains("._")) {
                            outputParam.modelList.add(new SingleFileVersionModel(cfile.getAbsolutePath()
                                    , cfile.length()
                                    , FileUtil.getMD5(cfile)));
                        }
                    } else {
                        outputParam.modelList.add(new SingleFileVersionModel(cfile.getAbsolutePath()
                                , cfile.length()
                                , FileUtil.getMD5(cfile)));
                    }

                }

            }
        } catch (Exception e) {
            System.out.println("dirFile:" + dirFile);
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }
//         if (VersionType.LEN.equals(versionType)) {
//        return "" + file.length();
//    } else {
//        return FileUtil.getMD5(file);
//    }

    private static void getMd5FileStr(File dirFile, DirMd5OutputParam outputParam, DirMd5Param dirMd5Param) {
//        File baseDir = dirMd5Param.checkBaseDirStr;
        try {
            File[] files = dirFile.listFiles();
//            var dirKey = FileUtil.getSubPathUnix(dirFile.toPath(), baseDir.toPath());
            //如果不是空文件夹，把父亲文件夹加入
            if (dirMd5Param.checkDir) {
                outputParam.md5Map.put(dirFile, DIR_VALUE);
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

//                    String key = FileUtil.getSubPathUnix(cfile.toPath(), baseDir.toPath());
                    if (ignoreMacShadowFile) {
                        if (!cfile.getAbsolutePath().contains("._")) {
                            outputParam.md5Map.put(cfile, getMd5(cfile, dirMd5Param.genType));
                        }
                    } else {
                        outputParam.md5Map.put(cfile, getMd5(cfile, dirMd5Param.genType));
                    }

                }

            }
        } catch (Exception e) {
            System.out.println("dirFile:" + dirFile);
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    public static String getMd5(File file, VersionType versionType) {
        if (VersionType.LEN.equals(versionType)) {
            return "" + file.length();
        } else {
            return FileUtil.getMD5(file);
        }
    }

    public static class DirMd5Param {
        private File checkBaseDir;
        private VersionType genType = VersionType.LEN;
        private FileAndDirFilter noNeedCalMd5FileFilter;
        private boolean checkDir = false;

        public File getCheckBaseDir() {
            return checkBaseDir;
        }

        public void setCheckBaseDir(File checkBaseDir) {
            this.checkBaseDir = checkBaseDir;
        }

        public VersionType getGenType() {
            return genType;
        }

        public void setGenType(VersionType genType) {
            this.genType = genType;
        }

        public FileAndDirFilter getNoNeedCalMd5FileFilter() {
            return noNeedCalMd5FileFilter;
        }

        public void setNoNeedCalMd5FileFilter(FileAndDirFilter noNeedCalMd5FileFilter) {
            this.noNeedCalMd5FileFilter = noNeedCalMd5FileFilter;
        }

        public boolean isCheckDir() {
            return checkDir;
        }

        public void setCheckDir(boolean checkDir) {
            this.checkDir = checkDir;
        }
    }

    private static record DirMd5OutputParam(LinkedHashMap<File, String> md5Map, AtomicLong count) {

    }

    private static record DirMd5OutputParam2(List<SingleFileVersionModel> modelList,
                                             List<SingleDirVersionModel> dirVersionModelList, AtomicLong count) {

    }
}
