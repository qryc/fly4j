package fnote.filebrowser.web.servlet;

import fly4j.common.http.FileDown;
import fnote.common.web.SpringContextHolder;
import fly4j.common.http.CookiesUtil;
import fly4j.common.util.BreakException;
import fnote.article.share.AuthShareServiceImpl;
import fnote.user.domain.entity.LoginUser;
import fnote.article.web.controller.PublishedController;
import fnote.user.domain.service.LoginService;
import fnote.common.StorePathService;
import fnote.web.common.FlyWebUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * 显示或下载图片，以及下载其它文件
 * 1.显示图片
 * http://xxx.com/pic/479/641.jpg
 * /viewFile?filepath=
 * /viewFile?absolutePath=
 * 根据479得到博客ID,如果有查看博客的权限，就有查看下面图片的权限
 */
public class NoteViewFileServlet extends HttpServlet {
    private final static Log log = LogFactory.getLog(NoteViewFileServlet.class);

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        LoginService loginService = SpringContextHolder.getBean("loginService");
        LoginUser loginUser = loginService.getLoginUserByCookieCheckedSession(request);
        StorePathService pathService = SpringContextHolder.getBean("pathService");
        String absolutePath = request.getParameter("absolutePath");
        final String relativePath = request.getParameter("relativePath") != null ? request.getParameter("relativePath").trim() : null;

        if (null != loginUser) {
            /**绝对路径*/

            if (StringUtils.isNotBlank(absolutePath)) {
                absolutePath = absolutePath.trim();
                //权限判定
                log.debug("absolutePath:" + URLDecoder.decode(absolutePath, StandardCharsets.UTF_8));
                FileDown.viewFile(response, Path.of(absolutePath).toFile());
            }
            /**相对路径*/

            if (StringUtils.isNotBlank(relativePath)) {
                log.debug("relativePath:" + URLDecoder.decode(relativePath, StandardCharsets.UTF_8));
                try {
                    Path filePath = pathService.getUserPicDirPaths(loginUser.pin()).stream()
                            .map(path -> path.resolve(URLDecoder.decode(relativePath, StandardCharsets.UTF_8)))
                            .filter(path -> Files.exists(path)).findFirst().get();

                    filter(response, relativePath, filePath);
                    FileDown.viewFile(response, filePath.toFile());
                } catch (BreakException e) {
                }
            }
        }
    }

    protected Path getDiskRootPath(HttpServletRequest request, HttpServletResponse response) throws IOException, BreakException {
//        return Path.of(System.getProperty("user.home"));
        /***
         * 未登录用户访问其它资源，直接返回
         */
        LoginService loginService = SpringContextHolder.getBean("loginService");
        StorePathService pathService = SpringContextHolder.getBean("pathService");
        LoginUser loginUser = loginService.getLoginUserByCookieCheckedSession(request);
        String viewPin = "";
        if (null == loginUser) {
            //共享码为空，不允许访问
            String shareCodeFromCookies = CookiesUtil.getCookieValue(request, PublishedController.SHARECODE);
            if (StringUtils.isBlank(shareCodeFromCookies)) {
                FlyWebUtil.sendRedirectMsg(response, "未授权用户访问!");
                throw new BreakException("未授权用户访问!");
            }

            //共享码对应的博客ID为空，不允许访问，只要有共享码，就可以访问所有图片
            AuthShareServiceImpl authShareService = SpringContextHolder.getBean("authShareService");
            AuthShareServiceImpl.ShareArticleInfo shareBlogIdTtl = authShareService.getShareArticleInfo(shareCodeFromCookies);
            if (null == shareBlogIdTtl) {
                FlyWebUtil.sendRedirectMsg(response, "共享内容不存在!");
                throw new BreakException("共享内容不存在!");
            }
            viewPin = shareBlogIdTtl.getPin();
        } else {
            viewPin = loginUser.pin();
        }
        if (loginUser.isAdmin()) {
            return Path.of(System.getProperty("user.home"));
        }
        return pathService.getURootPath(viewPin);
    }

    public void filter(HttpServletResponse response, String relativePath, Path filePath) throws IOException, BreakException {
        StorePathService pathService = SpringContextHolder.getBean("pathService");
        //必须是工作空间下的文件
        if (!pathService.isInStoreDir(filePath)) {
            throw new BreakException();
        }
        //文件浏览器可以调整查看文章页面,用repository支持
//        if (pathService.isFile(StorePathService.PATH_ARTICLE, filePath.getFileName().toString())) {
//            response.sendRedirect("article/viewArticle.do?noteFileStr=" + URLEncoder.encode(relativePath, StandardCharsets.UTF_8));
//            throw new BreakException();
//        }
    }
}
