package farticle.domain.view;

import fnote.domain.config.FlyContext;

import java.io.File;

public interface FileFilter {
    public boolean test(File file, FlyContext flyContext);
}
