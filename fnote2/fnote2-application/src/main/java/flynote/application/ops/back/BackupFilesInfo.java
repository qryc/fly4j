package flynote.application.ops.back;

import fly4j.common.file.FileInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 备份下载信息
 *
 * @author xxgw
 */
public class BackupFilesInfo {
    private List<FileInfo> downFileNames;// 下载文件的名称

    public BackupFilesInfo() {
        downFileNames = new ArrayList<FileInfo>();
    }

    public List<FileInfo> getDownFileNames() {
        return downFileNames;
    }

    public void setDownFileNames(List<FileInfo> downFileNames) {
        this.downFileNames = downFileNames;
    }


    public void sortDownFileNames() {
        Collections.sort(this.downFileNames, new Comparator<FileInfo>() {
            @Override
            public int compare(FileInfo o1, FileInfo o2) {
                return -o1.getFileName().compareTo(o2.getFileName());
            }
        });
    }


}
