package fnote.infrastructure.impl.file;

import java.nio.file.Path;

public interface RepoPathService {

    public Path getArticleDefaultPath(String pin, String userLabel);

    public Path getTempDirPath(String storeName);

}
