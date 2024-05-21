package fnote.filebrowser.web.servlet;

import fnote.common.web.SpringContextHolder;
import fnote.filebrowser.web.servlet.DownloadServlet;
import fly4j.common.util.BreakException;
import fnote.user.domain.entity.LoginUser;
import fnote.user.domain.service.LoginService;
import fnote.common.StorePathService;
import fnote.web.common.FlyWebUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Path;

public class NoteDownloadServlet extends DownloadServlet {
    @Override
    protected Path getDiskRootPath(HttpServletRequest request, HttpServletResponse response) throws IOException, BreakException {
        LoginService loginService = SpringContextHolder.getBean("loginService");
        StorePathService pathService = SpringContextHolder.getBean("pathService");
        LoginUser loginUser = loginService.getLoginUserByCookieCheckedSession(request);
        if (null == loginUser) {
            FlyWebUtil.sendRedirectMsg(response, "未授权用户不可访问!");
            throw new BreakException("未授权用户不可访问");
        }
        if (loginUser.isAdmin()) {
            return Path.of(System.getProperty("user.home"));
        }
        return pathService.getURootPath(StorePathService.PATH_ARTICLE, loginUser.pin());
    }
}
