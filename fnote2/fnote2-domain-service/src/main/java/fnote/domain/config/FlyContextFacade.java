package fnote.domain.config;

import fly4j.common.util.RepositoryException;
import fnote.user.domain.entity.UserConfig;
import fnote.user.domain.service.LoginService;
import fnote.user.domain.entity.LoginUser;
import fnote.user.domain.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FlyContextFacade {
    private LoginService loginService;
    private UserService userService;

    public FlyContext getFlyContext(HttpServletRequest request, HttpServletResponse resp) throws RepositoryException {
        return new FlyContext(getUserConfig(request), getLoginUser(request), getRequestConfig(request), getClientConfig(request, resp));
    }

    /**
     * 此处不使用Spring注入，是因为避免Spring依赖繁杂，直接使用方注入loginService，flyConfig，比注入FlyWebUtil更容易读懂
     */
    public LoginUser getLoginUser(HttpServletRequest request) {

        return loginService.getLoginUserByCookieCheckedSession(request);
    }

    public RequestConfig getRequestConfig(HttpServletRequest request) {
        return PageConfigUtil.getRequestConfig(request);
    }

    public ClientConfig getClientConfig(HttpServletRequest request, HttpServletResponse resp) {
        return PageConfigUtil.getClientConfig(request, resp);
    }

    public UserConfig getUserConfig(HttpServletRequest request) throws RepositoryException {
        var loginUser = getLoginUser(request);
        if (loginUser == null) {
            return null;
        }
        return userService.getUserConfig(loginUser.pin());
    }


    public void setLoginService(LoginService loginService) {
        this.loginService = loginService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
