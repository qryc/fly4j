package fnote.common;

import java.nio.file.Path;

public interface PathService {
    Path getRootDir();
    Path getUserDir(String pin);
    Path getConfigDir();

}
