package farticle.domain.view;

import fnote.domain.config.FlyContext;

import java.io.File;
import java.util.function.Predicate;

public class FilePredicate4Browser implements FileFilter {
    @Override
    public boolean test(File cfile, FlyContext flyContext) {
        if (cfile.isDirectory()) {
            if (cfile.getName().equals("pic") || cfile.getName().equals("picture")) {
                return true;
            } else {
                return true;
            }
        } else {
            if (cfile.getName().endsWith(".md") || cfile.getName().endsWith("flyNote") || cfile.getName().endsWith("txt")) {
                return true;
            } else {
                return true;
            }
        }

    }
}
