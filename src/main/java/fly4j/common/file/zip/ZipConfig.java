package fly4j.common.file.zip;

import fly4j.common.file.version.BackModel;

import java.io.File;
import java.nio.file.Path;
import java.util.function.Predicate;

/**
 * eg: /export/mecode/* --> /export/back/**.zip
 * dir:文件夹
 * pathStr：文件路径
 */
public record ZipConfig(File sourceDir, File destZipFile, String password, boolean delZip, BackModel.DigestType versionType,
                        Predicate<File> refusePredicate) {

    public static final String DEFAULT_VERSIONDATA_PATH = "versionData";


    public File getDefaultSourceMd5File() {
        File dirFile = Path.of(sourceDir().getAbsolutePath(), DEFAULT_VERSIONDATA_PATH).toFile();
        if (!dirFile.exists()) {
            dirFile.mkdir();
        }
        return dirFile;
    }

}
