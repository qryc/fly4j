package fly4j.common.http;

import fly4j.common.domain.FlyResult;
import fly4j.common.file.BroserUtil;
import fly4j.common.file.FileUtil;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by qryc on 2021/10/20
 */
public class FileDown {
    public static void downFileToResponse(HttpServletResponse response, File file) throws IOException {
        /* 如果文件存在 */
        if (!file.exists()) {
            throw new FileNotFoundException(file.getAbsolutePath());
        }
        response.reset();
        response.setContentType("application/x-msdownload");
        var filename = URLEncoder.encode(file.getName(), "utf-8");   //文件解码
        response.addHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
        response.setContentLength((int) file.length());

        FileUtil.writeFileToResponse(response.getOutputStream(), file);
    }

    public static void viewFile(HttpServletResponse response, File file) throws IOException {
        if (!file.exists()) {
            throw new FileNotFoundException(file.getAbsolutePath());
        }

        //文件解码
        response.reset();
        if (FileUtil.isImg(file)) {
            response.setContentType("image/jpeg");
        } else {
            if (BroserUtil.isPacked(file.getName(), false)) {
                downFileToResponse(response, file);
                return;
            }
            String mimeType = BroserUtil.getMimeType(file.getName());
            response.setContentType(mimeType);
            if (mimeType.equals("text/plain")) response.setHeader(
                    "Content-Disposition", "inline;filename=\"temp.txt\"");
            else response.setHeader("Content-Disposition", "inline;filename=\""
                    + file.getName() + "\"");
            response.setCharacterEncoding("UTF-8");
        }
        FileUtil.writeFileToResponse(response.getOutputStream(), file);
    }

}
