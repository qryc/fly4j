package fly.sample.login;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录拦截过滤器示例程序
 */
public class LoginFilter implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException,
            ServletException {
        var request = (HttpServletRequest) req;
        var response = (HttpServletResponse) res;

        // 解析Cookies信息得到登录用户信息
        LoginUser loginUser = getLoginUserByCookieCheckedSession(request);
        //判断集中式Session中是否存在,不存在登录失败
        boolean sessionSuccess = verifyLoginBySession(loginUser.sessionId());
        if (!sessionSuccess) {
            loginUser = null;
        }
        //未登录跳转登录页面
        if (null == loginUser) {
            var loginUrl = "/toLogin.do";
            response.sendRedirect(loginUrl);
            return;
        }
        //设置登录信息到Request域，后续可以使用
        request.setAttribute("loginUser", loginUser);
        request.setAttribute("pin", loginUser.pin());

        //如果是登录用户放行URL
        filterChain.doFilter(request, response);
    }

    private LoginUser getLoginUserByCookieCheckedSession(HttpServletRequest request) {
        return null;
    }

    boolean verifyLoginBySession(String sessionId) {
        return true;
    }

    public record LoginUser(String pin, String sessionId) {
    }


    @Override
    public void destroy() {

    }
}
