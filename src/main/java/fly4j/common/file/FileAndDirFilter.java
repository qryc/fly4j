package fly4j.common.file;


import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FileFilter;
import java.util.Set;


public class FileAndDirFilter implements FileFilter {
    //过滤的文件名称集合
    private Set<String> filterDirNames;
    //过滤文件类型集合
    private Set<String> filterSuffixNames;

    public FileAndDirFilter(Set<String> filterDirNames, Set<String> filterSuffixNames) {
        this.filterDirNames = filterDirNames;
        this.filterSuffixNames = filterSuffixNames;
    }

    @Override
    public boolean accept(File file) {
        if (null != filterDirNames) {
            for (String filterDir : this.filterDirNames) {
                if (file.isDirectory() && file.getName().contains(filterDir)) {
                    return true;
                }
            }
        }
        if (this.filterSuffixNames != null) {
            String suffix = FilenameUtils.getExtension(file.getName());
            return this.filterSuffixNames.contains(suffix);
        }
        return false;
    }
}
