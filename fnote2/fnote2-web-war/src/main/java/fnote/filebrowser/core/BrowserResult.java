package fnote.filebrowser.core;


import java.net.URLEncoder;
import java.nio.file.Path;

public class BrowserResult {
    private String parentDirStr;//上一级
    //当前路径
    private String currDirStr;
    private int sortMode4Pre = 1;//排序号
    // The total size of the files in the current directory
    public long totalSize = 0;
    // The count of files in the current working directory
    public long fileCount = 0;

    public BrowserResult(Path currDirPath, Path rootPath) {
        if (null != currDirPath) {
            var parentPath = currDirPath.getParent().startsWith(rootPath) ? currDirPath.getParent() : rootPath;
            //URLEncoder.encode(relativePath.toString())，展示会有%号转义
            this.setCurrDirStr(currDirPath.toString())
                    .setParentDirStr(parentPath.toString());
        }

    }


    public String getParentDirStr() {
        return parentDirStr;
    }

    public BrowserResult setParentDirStr(String parentDirStr) {
        this.parentDirStr = parentDirStr;
        return this;
    }


    public String getCurrDirStr() {
        return currDirStr;
    }

    public BrowserResult setCurrDirStr(String currDirStr) {
        this.currDirStr = currDirStr;
        return this;
    }

    public int getSortMode4Pre() {
        return sortMode4Pre;
    }

    public BrowserResult setSortMode4Pre(int sortMode4Pre) {
        this.sortMode4Pre = sortMode4Pre;
        return this;
    }

    public long getTotalSize() {
        return totalSize;
    }

    public BrowserResult setTotalSize(long totalSize) {
        this.totalSize = totalSize;
        return this;
    }

    public long getFileCount() {
        return fileCount;
    }

    public BrowserResult setFileCount(long fileCount) {
        this.fileCount = fileCount;
        return this;
    }
}
