package fly4j.common.file;


import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FileFilter;
import java.util.Set;


public class FileAndDirFilter implements FileFilter {
    //过滤的文件名称集合
    private Set<String> acceptDirOrFileNames;
    //过滤文件类型集合
    private Set<String> acceptExtensions;

    public FileAndDirFilter(Set<String> acceptDirOrFileNames, Set<String> acceptExtensions) {
        this.acceptDirOrFileNames = acceptDirOrFileNames;
        this.acceptExtensions = acceptExtensions;
    }

    @Override
    public boolean accept(File file) {
        if (null != acceptDirOrFileNames) {
            for (String filterDir : this.acceptDirOrFileNames) {
                if (file.isDirectory() && file.getName().contains(filterDir)) {
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
