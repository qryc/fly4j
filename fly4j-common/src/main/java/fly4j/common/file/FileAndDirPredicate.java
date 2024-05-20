package fly4j.common.file;


import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.Set;
import java.util.function.Predicate;


public class FileAndDirPredicate implements Predicate<File> {
    //过滤的文件名称集合
    private Set<String> acceptDirOrFileNames;
    //过滤文件类型集合
    private Set<String> acceptExtensions;

    public FileAndDirPredicate(Set<String> acceptDirOrFileNames, Set<String> acceptExtensions) {
        this.acceptDirOrFileNames = acceptDirOrFileNames;
        this.acceptExtensions = acceptExtensions;
    }

    @Override
    public String toString() {
        return "文件/文件夹名=" + acceptDirOrFileNames +
                ", 文件类型=" + acceptExtensions;

    }

    @Override
    public boolean test(File file) {
        if (null != acceptDirOrFileNames) {
            for (String filterDir : this.acceptDirOrFileNames) {
                if (file.getName().contains(filterDir)) {
                    return true;
                }
            }
        }
        if (this.acceptExtensions != null) {
            String extension = FilenameUtils.getExtension(file.getName());
            return this.acceptExtensions.contains(extension);
        }
        return false;
    }

}
