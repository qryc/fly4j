package fly4j.common.file;


import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 用来前台展示文件信息
 *
 * @author qryc
 */
public class FileInfo {
    //相对于下载网址的路径
    private final String fileRelativePath;
    private final boolean pic;
    //文件名称
    private final String fileName;
    //文件绝对路径
    private final String absolutePath;
    private final boolean directory;

    //扩展属性
    private Map<String, Object> extMap = new HashMap<>();

    public FileInfo(File file) {
        //绝对路径
        this.absolutePath = file.getAbsolutePath();
        this.pic = FileUtil.isImg(file);
        //相对路径
        String absolutePathLinux = file.getAbsolutePath().replaceAll(
                "\\\\", "/");
        if (absolutePathLinux.split("/pic/").length > 1) {
            this.fileRelativePath = absolutePathLinux.split("/pic/")[1];
        } else {
            this.fileRelativePath = absolutePathLinux;
        }

        this.fileName = file.getName();
        directory = file.isDirectory();
        if (!directory) {
            extMap.put("size", file.length());
            extMap.put("displaySize", FileUtils.byteCountToDisplaySize(file.length()));

        }
        extMap.put("lmDate", new Date(file.lastModified()));

        String type = "File"; // This String will tell the extension of the file
        if (file.isDirectory()) type = "DIR"; // It's a DIR
        else {
            String tempName = file.getName().replace(' ', '_');
            if (tempName.lastIndexOf('.') != -1) type = tempName.substring(
                    tempName.lastIndexOf('.')).toLowerCase();
        }
        extMap.put("type", type);


    }

    @Override
    public String toString() {
        return fileRelativePath;
    }


    public boolean isPacked() {
        return BroserUtil.isPacked(absolutePath, true);
    }

    public String getFileRelativePath() {
        return fileRelativePath;
    }

    public boolean isPic() {
        return pic;
    }

    public String getFileName() {
        return fileName;
    }

    public String getAbsolutePath() {
        return absolutePath;
    }

    public boolean isDirectory() {
        return directory;
    }

    public Map<String, Object> getExtMap() {
        return extMap;
    }
}
