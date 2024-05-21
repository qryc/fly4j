package fnote.web.controller;

import fly4j.common.http.CookiesUtil;
import fly4j.common.http.WebUtil;
import fly4j.common.http.ajax.FetchSend;
import fly4j.common.util.ExceptionUtil;
import fly4j.common.util.HtmlEscape;
import fnote.user.domain.infrastructure.UserRepository;
import fnote.user.domain.entity.UserInfo;
import fnote.user.domain.entity.LoginResultEnum;
import fnote.user.domain.service.LoginService;
import fnote.web.common.FlyWebUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录控制器
 *
 * @author qryc
 */
@Controller
@RequestMapping("/login")
public class LoginController {
    private static final Log log = LogFactory.getLog(LoginController.class);
    public static final String RETURN_URL = "ReturnUrl";
    @Resource
    private LoginService loginService;
    @Resource
    private UserRepository userRepository;

    /**
     * 博客所有者管理登录
     */
    @RequestMapping(value = "login.do")
    public String login(UserInfo user, HttpServletRequest req, HttpServletResponse resp, ModelMap context) throws Exception {
        // 执行登录
        LoginResultEnum logionResult = loginService.login(user.getPin(), user.getPassword(), WebUtil.getIp(req));
        log.debug("login:" + user.getPin() + " logionResult" + logionResult.viewMsg);

        // 如果失败提示失败信息
        if (!logionResult.isSuccess()) {
            return FlyWebUtil.getSendRedirectMsgPageSpringMvc(logionResult.viewMsg);
        }

        // 登录成功写cookies
        loginService.addLoginCookies(req, resp, logionResult);


        // 跳转成功页，写returnUrl用于登录后的跳转，跳转登录页之前写入
        String returnUrl = CookiesUtil.getCookieValue(req, RETURN_URL);
        if (StringUtils.isBlank(returnUrl)) {
            returnUrl = WebUtil.getDomainUrl(req);
        }
        // 删除cookies中的returnUrl
        CookiesUtil.clearCookie(req, resp, RETURN_URL);


        //跳转returnUrl
//        flyWebUtil.sendRedirect(resp, returnUrl);
        //修改为跳转主页
        var flyUserInfo = (UserInfo) userRepository.getUserinfo(logionResult.loginUser.pin());
        String userHomePage = flyUserInfo.getUserConfig().homePage;

        return "redirect:/userConfig/toBrowserSet.do";
    }


    /**
     * 注销
     */
    @RequestMapping(value = "loginOut.do")
    public String loginOut(UserInfo user, HttpServletRequest req, HttpServletResponse resp, ModelMap context) {
        loginService.loginOut(user, req, resp);
        // 写returnUrl用于登录后的跳转，跳转登录页之前写入
        String returnUrl = CookiesUtil.getCookieValue(req, RETURN_URL);
        if (StringUtils.isBlank(returnUrl)) {
            returnUrl = WebUtil.getDomainUrl(req);
        }

        WebUtil.sendRedirect(resp, returnUrl);
        return "redirect:/userConfig/toBrowserSet.do";
    }


    /**
     * 跳转到登录页
     */
    @RequestMapping(value = "toLogin.do")
    public String toLogin(Long id, HttpServletRequest req, HttpServletResponse resp, ModelMap context) {
        //把来源url写入cookies中，登录成功后跳转回去
        String returnUrlHtml = req.getParameter(RETURN_URL);
        if (StringUtils.isNotEmpty(returnUrlHtml)) {
            String returnUrl = HtmlEscape.escape(returnUrlHtml);
            CookiesUtil.addCookie(resp, RETURN_URL, returnUrl, 10000000, false);
        }
        return "fu/login/login";
    }


    /**
     * 发送动态密码
     */
    @RequestMapping(value = "sendDynPwd.do")
    public void sendDynPwd(UserInfo user, HttpServletRequest req, HttpServletResponse resp, ModelMap context) {
        FetchSend ajaxSend = () -> {
            return ExceptionUtil.wrapperRuntimeR(() -> {
                if (StringUtils.isBlank(user.getPin()))
                    return "pin is empty";
                loginService.addDynPwd(user.getPin(), WebUtil.getIp(req));
                return "发送成功";
            });
        };
        ajaxSend.doAjax(req, resp);
    }

    /**
     * 发送动态密码
     */
    @RequestMapping(value = "testSendDynPwd.do")
    public void testSendDynPwd(UserInfo user, HttpServletRequest req, HttpServletResponse resp, ModelMap context) {
        FetchSend ajaxSend = () -> {
            return "发送成功";
        };
        ajaxSend.doAjax(req, resp);
    }

}
