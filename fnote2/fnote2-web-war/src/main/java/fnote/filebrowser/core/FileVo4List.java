package fnote.filebrowser.core;


import fly4j.common.file.BroserUtil;
import fly4j.common.file.FileUtil;
import fly4j.common.util.DateUtil;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Date;

/**
 * 用来前台展示文件信息
 *
 * @author qryc
 */
public class FileVo4List {
    String checkBox;
    String hrefTitle;
    String fileOperations;
    String dayStrCn;
    String displaySize;

    public FileVo4List(String hrefTitle, String fileOperations) {
        this.hrefTitle = hrefTitle;
        this.fileOperations = fileOperations;
    }

    public FileVo4List() {
    }

    private static final String downFilePathHref = """
             <a onmousedown=dis() href="/download?relativePath=%s" class="graySecondFont">下载</a>
            """;
    private static final String checkBox_Pattern = """
             <input type="checkbox" name="selectFiles"  value='%s' onmousedown="dis()" onchange="showButton()">
            """;
    private static final String dirHref = """
            <!--文件夹标题区域-->
            <a onmousedown="dis()"  href="/browser/list.do?sort=%s&currDir=%s"> [%s]</a>
               """;
    private static final String fileViewHref = """
                       <a onmousedown="dis()"  href="/viewFile?absolutePath=%s" target="_blank">%s</a>
            """;
    private static final String imageHref = """
            <img src='/viewFile?absolutePath=%s' width=200   alt='%s'/>
            """;
    private static final String imageSizeHref = """
            <img src='/viewFile?absolutePath=%s' width=%s   alt='%s'/>
            """;

    public static String buildHrefTitle(BrowserResult browserResult, File file, Path pinPath) {
        String absoolutePathEnStr = URLEncoder.encode(file.getAbsolutePath(), StandardCharsets.UTF_8);
        StringBuilder builder = new StringBuilder();
        //拼接选择框
        if (file.isDirectory()) {
            //拼接文件夹链接
            builder.append(dirHref.formatted(browserResult.getSortMode4Pre(), absoolutePathEnStr, file.getName()));
        } else {
            boolean packed = BroserUtil.isPacked(file.getName(), true);
            if (packed) {
                builder.append(file.getName());
            } else {
                builder.append(fileViewHref.formatted(absoolutePathEnStr, file.getName()));
            }
            if (FileUtil.isImg(file)) {
                builder.append(imageHref.formatted(absoolutePathEnStr, file.getName()));
            }
        }
        builder.append("<br/>");


        return builder.toString();
    }

    public static String buildImagHref(File file, int size) {
        String relativePathEnStr = URLEncoder.encode(file.getAbsolutePath());
//        String relativePathEnStr = file.getAbsolutePath();
        //        builder.append(" <a onmousedown=\"dis()\"  href=\"/viewFile?absolutePath=%s\" target=\"_blank\">%s</a>".formatted(relativePathEnStr, file.getAbsolutePath()));
        //        builder.append(FileUtils.byteCountToDisplaySize(file.length()));
        return "<img src='/viewFile?absolutePath=%s' width=%s   alt='%s'/>".formatted(relativePathEnStr, size, file.getAbsolutePath())
//        builder.append(FileUtils.byteCountToDisplaySize(file.length()));
                ;
    }

    public static String buildFileOperations(File file, Path pinPath, String pin) {
        String relativePathStr = pinPath.relativize(file.toPath()).toString();
        StringBuilder builder = new StringBuilder();
        builder.append("""
                 <a href="#" onclick="copyText('http://fly4j.cn/viewFile?relativePath=%s&viewPin=%s')" class="graySecondFont">复制相对地址链接</a>
                """.formatted(relativePathStr, pin));
//        builder.append("""
//                 <a href="#" onclick="copyText('http://fly4j.cn/viewFile?absolutePath=%s&viewPin=%s')" class="graySecondFont">复制绝对地址链接</a>
//                """.formatted(file.getAbsolutePath(), pin));
        builder.append("""
                 <a href="/jsp/checkVersion.jsp?rightDirInput=%s"  class="graySecondFont">查看文件夹MD5</a>
                """.formatted(URLEncoder.encode(file.getAbsolutePath(), StandardCharsets.UTF_8)));
        builder.append("""
                 <a href="/jsp/checkDirVersion.jsp?checkDir=%s"  class="graySecondFont">检查文件夹MD5</a>
                """.formatted(URLEncoder.encode(file.getAbsolutePath(), StandardCharsets.UTF_8)));
        if (file.isFile()) {
            builder.append(downFilePathHref.formatted(URLEncoder.encode(relativePathStr)));
            builder.append(downFilePathHref.formatted(URLEncoder.encode(relativePathStr)));
        }
        return builder.toString();
    }

    public static FileVo4List buildFileInfo(BrowserResult browserResult, File file, Path rootPath, String pin) {
        FileVo4List result = new FileVo4List();
        //构建文件标题链接
        result.setHrefTitle(buildHrefTitle(browserResult, file, rootPath));
        //构建文件操作
        result.setFileOperations(buildFileOperations(file, rootPath, pin));

        //拼接选择框
        String absolutePathEnStr = URLEncoder.encode(file.getAbsolutePath());
        result.checkBox = checkBox_Pattern.formatted(absolutePathEnStr);
        result.dayStrCn = DateUtil.getDayStrCn(new Date(file.lastModified()));

        if (file.isFile()) {
            result.displaySize = FileUtils.byteCountToDisplaySize(file.length());
        }
        return result;
    }

    public String getHrefTitle() {
        return hrefTitle;
    }

    public void setHrefTitle(String hrefTitle) {
        this.hrefTitle = hrefTitle;
    }

    public String getFileOperations() {
        return fileOperations;
    }

    public void setFileOperations(String fileOperations) {
        this.fileOperations = fileOperations;
    }

    public String getCheckBox() {
        return checkBox;
    }

    public String getDayStrCn() {
        return dayStrCn;
    }

    public String getDisplaySize() {
        return displaySize;
    }
}
