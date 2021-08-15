package fly4j.common.back;

import fly4j.common.lang.DateUtil;

import java.io.File;
import java.nio.file.Path;
import java.util.Date;

/**
 * eg: /export/mecode/* --> /export/back/**.zip
 * dir:文件夹
 * path：文件路径
 */
public class ZipConfig {
    private File beZipSourceDir;
    private File destZipFile;
    private String password;
    private boolean delZip = true;
    private VersionType versionType = VersionType.LEN;

    public static final String DEFAULT_VERSIONDATA_PATH = "versionData";

    public ZipConfig() {
    }

    public ZipConfig(File beZipSourceDir, File destZipFile, String password) {
        this.beZipSourceDir = beZipSourceDir;
        this.destZipFile = destZipFile;
        this.password = password;
    }


    public File getDefaultSourceMd5File() {
        File dirFile = Path.of(getBeZipSourceDir().getAbsolutePath(), DEFAULT_VERSIONDATA_PATH).toFile();
        if (!dirFile.exists()) {
            dirFile.mkdir();
        }
        return dirFile;
    }

    public File getBeZipSourceDir() {
        return beZipSourceDir;
    }

    public ZipConfig setBeZipSourceDir(File beZipSourceDir) {
        this.beZipSourceDir = beZipSourceDir;
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
