package fly4j.common.back.version;

import fly4j.common.file.FileAndDirFilter;
import fly4j.common.file.FileUtil;
import fly4j.common.lang.DateUtil;
import fly4j.common.lang.FlyString;
import fly4j.common.os.OsUtil;
import fly4j.common.pesistence.file.FileJsonStrStore;

import java.io.File;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by qryc on 2021/9/6
 */
public class DirVersionGen {
    public static boolean ignoreMacShadowFile = true;

    public static String getDefaultVersionFileName(String beZipSourceDir) {
        if (null == beZipSourceDir) {
            beZipSourceDir = "";
        }
        return FlyString.getPlanText(beZipSourceDir) + DateUtil.getHourStr4Name(new Date()) + ".md5";
    }

    public static DirDigestAllModel saveDirVersionModel2File(String checkDirStr, FileAndDirFilter noNeedCalMd5FileFilter, Path md5StorePath) {
        DirVersionGenParam checkParam = new DirVersionGenParam(checkDirStr,
                noNeedCalMd5FileFilter,
                DateUtil.getDateStr(new Date()));
        DirDigestAllModel dirVersionModel = DirVersionGen.genDirVersionModel(checkParam);
        FileJsonStrStore.saveObject(md5StorePath, dirVersionModel);
        return dirVersionModel;
    }

    public static DirDigestAllModel genDirVersionModel(DirVersionGenParam checkParam) {
        //文件版本信息
        List<FileDigestModel> fileList = new ArrayList<>();
        List<DirDigestModel> dirList = new ArrayList<>();
        DirVersionGen.DirMd5OutputParam2 outputParam2 = new DirVersionGen.DirMd5OutputParam2(fileList, dirList, new AtomicLong(0));
        DirVersionGen.genDirVersionModelInner(new File(checkParam.checkBaseDirStr()), outputParam2, checkParam);

        //环境信息
        Map<String, String> environment = new HashMap<>();
        environment.put("os.name", OsUtil.getOsName());
        //结果信息
        environment.put("files.size", "" + fileList.size());
        environment.put("dir.size", "" + dirList.size());

        return new DirDigestAllModel(environment, checkParam, fileList, dirList);
    }

    private static void genDirVersionModelInner(File dirFile, DirVersionGen.DirMd5OutputParam2 outputParam, DirVersionGenParam checkParam) {
        try {
            File[] files = dirFile.listFiles();
            //如果不是空文件夹，把父亲文件夹加入
            outputParam.dirVersionModelList.add(new DirDigestModel(dirFile.getAbsolutePath(),
                    Long.valueOf(files.length)));

            for (File cfile : files) {
                if (null != checkParam.noNeedCalMd5FileFilter() && checkParam.noNeedCalMd5FileFilter().accept(cfile)) {
                    continue;
                }
                if (cfile.isDirectory()) {
                    //递归
                    genDirVersionModelInner(cfile, outputParam, checkParam);
                } else {
                    //生成md5
                    Long count = outputParam.count.incrementAndGet();
                    System.out.println("check file " + count + " :" + cfile.getAbsolutePath());
                    if (ignoreMacShadowFile) {
                        if (!cfile.getAbsolutePath().contains("._")) {
                            outputParam.modelList.add(new FileDigestModel(cfile.getAbsolutePath()
                                    , cfile.length()
                                    , FileUtil.getMD5(cfile)));
                        }
                    } else {
                        outputParam.modelList.add(new FileDigestModel(cfile.getAbsolutePath()
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

    private static record DirMd5OutputParam2(List<FileDigestModel> modelList,
                                             List<DirDigestModel> dirVersionModelList, AtomicLong count) {

    }
}
