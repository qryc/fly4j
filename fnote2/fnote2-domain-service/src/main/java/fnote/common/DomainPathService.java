package fnote.common;

import fnote.domain.config.FlyContext;

import java.nio.file.Path;
import java.util.List;

public interface DomainPathService {
    Path getRootDirPath();

    Path getUserDirPath(String pin);

    List<Path> getUserArticleDirPaths(String pin);

    Path getUTempRootPath(String pin);

    Path getUserBackDirPath(String pin);

    boolean isInStoreDir(Path absolutePath);

    List<Path> getUserPicDirPaths(String pin);

    List<Path> getUserDiskDirPaths(FlyContext flyContext);

    List<Path> getFlyDiskCanSelect(FlyContext flyContext);
}
