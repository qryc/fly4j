package fly4j.common.file;


import fly4j.common.file.BroserUtil;
import fly4j.common.file.FileUtil;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.file.Path;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 用来前台展示文件信息
 *
 * @author qryc
 */
public class FileInfo {
    //对外隐藏
    private Path path;
    //相对于下载网址的路径
    private final String relativePathStr;
    private final boolean pic;
    //文件名称
    private final String fileName;
    private final boolean directory;

    //扩展属性
    private Map<String, Object> extMap = new HashMap<>();

    public FileInfo(File file, Path pinPath) {
        this.path = file.toPath();
        this.pic = FileUtil.isImg(file);
        //相对路径
        this.relativePathStr = pinPath.relativize(file.toPath()).toString();

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
        return relativePathStr;
    }


    public boolean isPacked() {
        return BroserUtil.isPacked(path.toString(), true);
    }

    public String getRelativePathStr() {
        return relativePathStr;
    }

    public boolean isPic() {
        return pic;
    }

    public String getFileName() {
        return fileName;
    }


    public boolean isDirectory() {
        return directory;
    }

    public Map<String, Object> getExtMap() {
        return extMap;
    }
}
