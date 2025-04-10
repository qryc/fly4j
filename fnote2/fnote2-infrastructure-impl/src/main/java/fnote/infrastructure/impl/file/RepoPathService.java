package fnote.infrastructure.impl.file;

import fnote.common.DomainPathService;

import java.nio.file.Path;

public interface RepoPathService extends DomainPathService {

    public Path getArticleDefaultPath(String pin, String userLabel);

    public Path getTempDirPath(String storeName);

}
