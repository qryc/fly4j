package fly4j.common.back.version;

import fly4j.common.file.FileAndDirFilter;
import fly4j.common.file.FileUtil;
import fly4j.common.lang.DateUtil;
import fly4j.common.lang.FlyString;
import fly4j.common.os.OsUtil;
import fly4j.common.pesistence.file.FileJsonStrStore;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by qryc on 2021/9/6
 */
public class DirVersionGen {

    public static String getDefaultVersionFileName(String beZipSourceDir) {
        if (null == beZipSourceDir) {
            beZipSourceDir = "";
        }
        return FlyString.getPlanText(beZipSourceDir) + DateUtil.getHourStr4Name(new Date()) + ".md5";
    }

    public static BackModel.DirDigestAllModel saveDirVersionModel2File(String checkDirStr, FileAndDirFilter noNeedCalMd5FileFilter, Path md5StorePath) throws IOException {
        BackModel.DirVersionGenParam checkParam = new BackModel.DirVersionGenParam(checkDirStr,
                noNeedCalMd5FileFilter,
                DateUtil.getDateStr(new Date()));
        BackModel.DirDigestAllModel dirVersionModel = DirVersionGen.genDirVersionModel(checkParam);
        FileJsonStrStore.saveObject(md5StorePath, dirVersionModel);
        return dirVersionModel;
    }

    public static BackModel.DirDigestAllModel genDirVersionModel(BackModel.DirVersionGenParam checkParam) {
        //文件版本信息
        AtomicLong count = new AtomicLong(0);
        List<BackModel.FileDigestModel> fileList = new ArrayList<>();
        File dirFile = new File(checkParam.checkBaseDirStr());
        FileUtil.walkAllFileIgnoreMacShadowFile(dirFile, checkParam.noNeedCalMd5FileFilter(), file -> {
            //生成md5
            count.incrementAndGet();
            //生成md5
            System.out.println("check file " + count + " :" + file.getAbsolutePath());
            fileList.add(new BackModel.FileDigestModel(file.getAbsolutePath()
                    , file.length()
                    , FileUtil.getMD5(file)));
        });


        //环境信息
        Map<String, String> environment = new LinkedHashMap<>();
        environment.put("os.name", OsUtil.getOsName());
        //结果信息
        environment.put("files.size", "" + fileList.size());
        environment.put("checkParam.checkDate", "" + checkParam.checkDate());
        environment.put("checkParam.noNeedCalMd5FileFilter", "" + checkParam.noNeedCalMd5FileFilter());
        return new BackModel.DirDigestAllModel(environment, checkParam.checkBaseDirStr(), fileList);
    }


}
