package fly4j.common.back.check;

import fly4j.common.file.FileAndDirFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * alter by qryc in 2020/07/04
 * 不再先拷贝，删除不需要备份文件，再压缩，直接压缩，通过提前规划好需要备份和不需要备份文件
 */
public class DirZipService {
    static final Logger log = LoggerFactory.getLogger(DirZipService.class);
    private boolean afterTest = true;
    private DirCompareService dirCompare;
    private FileAndDirFilter noNeedCalMd5FileFilter;
    private boolean checkEmptyDir = false;

//    public FlyResult zipDirWithVerify(ZipConfig zipConfig) {
//        FlyResult backResult = new FlyResult().success();
//        try {
//            //生成MD5摘要文件
//            Path md5StorePath = Path.of(zipConfig.getDefaultSourceMd5File().toString(), DirVersionGen.getDefaultVersionFileName(zipConfig.getSourceDir().getAbsolutePath()));
//            DirVersionModel dirVersionModel = DirVersionGen.saveDirVersionModel2File(zipConfig.getSourceDir().toString(), noNeedCalMd5FileFilter, md5StorePath);
//            //执行备份 backFile
//            Zip4jTool.zipDir(zipConfig.getDestZipFile(), zipConfig.getSourceDir(), zipConfig.getPassword());
//            backResult.append("executeBack success srcFile(" + zipConfig.getSourceDir()).append(") zipe to (")
//                    .append(zipConfig.getDestZipFile().getAbsolutePath()).append(")")
//                    .append(StringUtils.LF);
//
//            //执行Test
//            if (afterTest) {
//                var checkResult = checkZip(zipConfig);
//                backResult.merge(checkResult);
//
//            }
//        } catch (Exception e) {
//            log.error("Zip4jTool.zip  srcFile:" + zipConfig.getSourceDir(), e);
//            backResult.append("Zip4jTool.zip  srcFile:" + zipConfig.getSourceDir()).append(" error ").append(e.getMessage()).append(StringUtils.LF);
//        }
//        return backResult;
//    }


//    private FlyResult checkZip(ZipConfig zipConfig) throws Exception {
//        File zipFile = zipConfig.getDestZipFile();
//        var backResult = new FlyResult().success();
//        var builder = new StringBuilder();
//        var unzipDirName = "unzipT4"
//                + zipFile.getName().replaceAll("\\.", "_");
//        var unzipDestDirPath = Path.of(zipFile.getParent(), unzipDirName);
//        Zip4jTool.unZip(zipFile, unzipDestDirPath.toFile(), zipConfig.getPassword());
//        builder.append("executeUnzip  (")
//                .append(zipFile.getAbsolutePath())
//                .append(")  to (")
//                .append(unzipDestDirPath.toString())
//                .append(")")
//                .append(StringUtils.LF);
//        var checkPath = Path.of(unzipDestDirPath.toString(), zipConfig.getSourceDir().getName());
//        var md5Path = Path.of(unzipDestDirPath.toFile().getAbsolutePath(), zipConfig.getSourceDir().getName(), ZipConfig.DEFAULT_VERSIONDATA_PATH);
//        FlyResult result = dirCompare.checkDirChange(checkPath.toFile(), FileUtil.getDirLastModifyFile(md5Path.toFile(), ".md5"), zipConfig.getVersionType());
//        builder.append("executeCheckVersion:" + checkPath.toFile().getAbsolutePath()).append(StringUtils.LF);
//        if (result.isSuccess()) {
//            builder.append("*******check ok").append(StringUtils.LF);
//        } else {
//            builder.append("******check fail!!!!!!!!!!!!").append(StringUtils.LF);
//            backResult.fail();
//        }
//        builder.append(result.getMsg()).append(StringUtils.LF);
//
//        return backResult.append(builder.toString());
//    }

    public boolean isAfterTest() {
        return afterTest;
    }

    public DirZipService setAfterTest(boolean afterTest) {
        this.afterTest = afterTest;
        return this;
    }

    public DirCompareService getDirCompare() {
        return dirCompare;
    }

    public DirZipService setDirCompare(DirCompareService dirCompare) {
        this.dirCompare = dirCompare;
        return this;
    }
}
