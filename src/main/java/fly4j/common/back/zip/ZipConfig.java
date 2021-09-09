package fly4j.common.back.zip;

import fly4j.common.back.version.BackModel;
import fly4j.common.file.FileAndDirFilter;

import java.io.File;
import java.nio.file.Path;

/**
 * eg: /export/mecode/* --> /export/back/**.zip
 * dir:文件夹
 * pathStr：文件路径
 */
public record ZipConfig(File sourceDir, File destZipFile, String password, boolean delZip, BackModel.DigestType versionType,
                        FileAndDirFilter noNeedCalMd5FileFilter) {

    public static final String DEFAULT_VERSIONDATA_PATH = "versionData";


    public File getDefaultSourceMd5File() {
        File dirFile = Path.of(sourceDir().getAbsolutePath(), DEFAULT_VERSIONDATA_PATH).toFile();
        if (!dirFile.exists()) {
            dirFile.mkdir();
        }
        return dirFile;
    }

}
