package fly4j.common.back.model;

import java.io.File;
import java.nio.file.Path;

/**
 * eg: /export/mecode/* --> /export/back/**.zip
 * dir:文件夹
 * pathStr：文件路径
 */
public class ZipConfig {
    private File sourceDir;
    private File destZipFile;
    private String password;
    private boolean delZip = true;
    private VersionType versionType = VersionType.LEN;

    public static final String DEFAULT_VERSIONDATA_PATH = "versionData";

    public ZipConfig() {
    }

    public ZipConfig(File sourceDir, File destZipFile, String password) {
        this.sourceDir = sourceDir;
        this.destZipFile = destZipFile;
        this.password = password;
    }


    public File getDefaultSourceMd5File() {
        File dirFile = Path.of(getSourceDir().getAbsolutePath(), DEFAULT_VERSIONDATA_PATH).toFile();
        if (!dirFile.exists()) {
            dirFile.mkdir();
        }
        return dirFile;
    }

    public File getSourceDir() {
        return sourceDir;
    }

    public ZipConfig setSourceDir(File sourceDir) {
        this.sourceDir = sourceDir;
        return this;
    }

    public File getDestZipFile() {
        return destZipFile;
    }

    public ZipConfig setDestZipFile(File destZipFile) {
        this.destZipFile = destZipFile;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public ZipConfig setPassword(String password) {
        this.password = password;
        return this;
    }

    public boolean isDelZip() {
        return delZip;
    }

    public ZipConfig setDelZip(boolean delZip) {
        this.delZip = delZip;
        return this;
    }

    public void setVersionType(VersionType versionType) {
        this.versionType = versionType;
    }

    public VersionType getVersionType() {
        return versionType;
    }
}
