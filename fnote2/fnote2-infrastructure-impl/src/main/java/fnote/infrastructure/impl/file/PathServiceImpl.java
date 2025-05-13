package fnote.infrastructure.impl.file;

import fnote.common.PathService;
import fnote.common.PropertisUtil;
import fnote.domain.config.FlyContext;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

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
