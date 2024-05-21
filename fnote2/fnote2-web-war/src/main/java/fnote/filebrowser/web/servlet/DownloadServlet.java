package fnote.filebrowser.web.servlet;

import fly4j.common.http.FileDown;
import fly4j.common.util.BreakException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Path;

/**
 * 显示或下载图片，以及下载其它文件
 * 1.显示图片
 * http://xxx.com/download?filepath=down2017-11-17--20-00-01.zip
 * absolutePath=
 * 根据479得到博客ID,如果有查看博客的权限，就有查看下面图片的权限
 */
public class DownloadServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //        String absolutePath = request.getParameter("absolutePath");
//        if (StringUtils.isBlank(absolutePath)) {
//            //地址：/pic/479/641.jpg
//            String filepath = request.getParameter("filepath");
//            absolutePath = FlyPathConfig.getUserDirPath(pin) + "/" + filepath;
//        }
        //typora拷贝到fly中，传递到此处转换中,不知道为什么出现了空格，直接替换
        String relativePath = request.getParameter("relativePath").replaceAll(" ", "");


        try {
            Path filePath = getDiskRootPath(request, response).resolve(relativePath);
            FileDown.downFileToResponse(response, filePath.toFile());
        } catch (BreakException e) {
        }

    }

    protected Path getDiskRootPath(HttpServletRequest request, HttpServletResponse response) throws IOException, BreakException {
        return Path.of(System.getProperty("user.home"));
    }

}
