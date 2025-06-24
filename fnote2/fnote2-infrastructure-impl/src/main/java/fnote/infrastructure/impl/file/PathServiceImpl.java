package fnote.infrastructure.impl.file;

import fnote.common.PathService;
import fnote.common.PropertisUtil;

import java.nio.file.Path;

/**
 * RootDir:------------/Volumes/myBulldozer
 * ConfigDir:          /Volumes/myBulldozer/.bullDozer
 * xxx'UserDir:-----/Volumes/myBulldozer/xxxUser
 */
public class PathServiceImpl implements PathService {
    /// 路径配置
    private Path rootDirPath;

    @Override
    public Path getRootDir() {
        return rootDirPath;
    }

    @Override
    public Path getUserDir(String pin) {
        return rootDirPath.resolve(pin);
    }

    @Override
    public Path getConfigDir() {
        return rootDirPath.resolve(".bullDozer");
    }


    /**
     * ****** getter and setter below **********
     */


    public void setRootDirPath(String rootDirPath) {
        this.rootDirPath = Path.of(PropertisUtil.adjustPath(rootDirPath));
    }


}
