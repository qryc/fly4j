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

    public ZipConfig() {
    }

    public ZipConfig(File beZipSourceDir, File destZipFile, String password) {
        this.beZipSourceDir = beZipSourceDir;
        this.destZipFile = destZipFile;
        this.password = password;
    }

    /**
     * 版本文件存放路径
     */
    public Path getSourceMd5DirName() {
        return Path.of(getBeZipSourceDir().toString(), ".flyMd5");
    }

    public File getSourceMd5File() {
        File dirFile = this.getSourceMd5DirName().toFile();
        if (!dirFile.exists()) {
            dirFile.mkdir();
        }
        return Path.of(this.getSourceMd5DirName().toString(), DateUtil.getHourStr4Name(new Date()) + ".md5").toFile();
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
}
