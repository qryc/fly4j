package farticle.domain.entity;

import java.nio.file.Path;

public class ArticleFileParam {
    public Path rootPath;

    public Path getRootPath() {
        return rootPath;
    }

    public void setRootPath(Path rootPath) {
        this.rootPath = rootPath;
    }
}
