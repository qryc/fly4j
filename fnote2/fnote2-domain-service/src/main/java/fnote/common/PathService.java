package fnote.common;

import java.nio.file.Path;

/**
 * 配置文件路径和用户数据路径已经完全分开。
 */
public interface PathService {
    /**
     * 返回系统根目录，也就是下面两个目录的父目录
     */
    Path getRootDir();

    /**
     * 返回用户数据目录
     * @param pin
     */
    Path getUserDir(String pin);

    /**
     * 返回系统配置文件路径
     */
    Path getConfigDir();

}
