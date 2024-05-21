package fnote.web.controller;

import fly4j.common.http.ajax.FetchSend;
import fly4j.common.mail.MailUtil2;
import fly4j.common.util.ExceptionUtil;
import fnote.common.StorePathService;
import fnote.domain.config.ClientConfig;
import fnote.domain.config.FlyContext;
import fnote.domain.config.FlyContextFacade;
import fnote.domain.config.PageConfigUtil;
import farticle.domain.view.TreeService;
import fnote.user.domain.entity.LoginUser;
import fnote.user.domain.entity.UserInfo;
import fnote.user.domain.service.LoginService;
import fnote.user.domain.service.UserService;
import fnote.user.domain.infrastructure.UserRepository;
import fly4j.common.util.RepositoryException;
import fnote.user.domain.entity.UserConfig;
import fnote.web.common.FlyWebUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 博客分类控制器
 *
 * @author qryc
 */
@Controller
@RequestMapping("/userConfig")
public class UserConfigController extends MenuController {
    static final Logger log = LoggerFactory.getLogger(UserConfigController.class);
    @Resource
    private UserRepository userRepository;
    @Resource
    private UserService userService;
    @Resource
    private LoginService loginService;
    @Resource
    private FlyContextFacade flyContextFacade;
    @Resource
    private MailUtil2 mailUtil;
    @Resource
    private TreeService dtreeUtil;
    @Resource
    private StorePathService pathService;


    @RequestMapping(value = "toPersonalSet.do")
    public String toPersonalSet(HttpServletRequest req, HttpServletResponse resp, ModelMap context) throws RepositoryException, IOException {
        FlyContext flyContext = flyContextFacade.getFlyContext(req, resp);
        if (null == flyContext.loginUser()) {
            FlyWebUtil.sendRedirectMsg(resp, "guest can't config info");
            return null;
        }
        //加载用户全局配置信息，所有浏览器都有效
        context.put("userInfo", userRepository.getUserinfo(flyContext.getPin()));
        context.put("backAddr", "/downloadOpen?flyClientToken=flyPas_s123096sd_4");
        context.put("menu", getMenu(req, flyContext));
        return "fu/config/toPersonalSet";
    }

    @RequestMapping(value = "updatePersonalSet.do")
    public String updatePersonalSet(UserConfig userConfig, HttpServletRequest req, HttpServletResponse resp, ModelMap context) throws RepositoryException, IOException {
        FlyContext flyContext = flyContextFacade.getFlyContext(req, resp);
        if (null == flyContext.loginUser()) {
            FlyWebUtil.sendRedirectMsg(resp, "guest can't updateConfig info");
            return null;
        }
        Map<String, String> extMap = new HashMap<>();
        extMap.put(UserInfo.EXT_ZIPPWD, req.getParameter("zipPwd"));
        extMap.put(UserInfo.EXT_AJAXSAVE, req.getParameter("ajaxSave"));
        userService.updateUserConfig(flyContext.getPin(), userConfig, extMap);
        return "redirect:/userConfig/toPersonalSet.do";
    }

    @RequestMapping(value = "toBrowserSet.do")
    public String toBrowserSet(HttpServletRequest req, HttpServletResponse resp, ModelMap context) throws RepositoryException, IOException {
        FlyContext flyContext = flyContextFacade.getFlyContext(req, resp);
        if (null == flyContext.loginUser()) {
            FlyWebUtil.sendRedirectMsg(resp, "guest can't config info");
            return null;
        }

        try {
            //设置选择项目
            List<String> selectDirs = new ArrayList<>();

            selectDirs.addAll(pathService.getFlyDiskCanSelect(flyContext).stream().map(path -> path.toFile().getAbsolutePath())
                    .collect(Collectors.toList()));
            context.put("selectDirs", selectDirs);
            //加载用户页面配置信息，只对当前浏览器有效
            context.put("clientConfig", flyContext.clientConfig());
            context.put("menu", getMenu(req, flyContext));
        } catch (Exception e) {
            log.error("err", e);
        }
        return "fu/config/toBrowserSet";
    }

    @RequestMapping(value = "updateBrowserSet.do")
    public String updateBrowserSet(ClientConfig clientConfig, HttpServletRequest req, HttpServletResponse resp, ModelMap context) throws RepositoryException, IOException {
        FlyContext flyContext = flyContextFacade.getFlyContext(req, resp);
        if (null == flyContext.loginUser()) {
            FlyWebUtil.sendRedirectMsg(resp, "guest can't updateConfig info");
            return null;
        }


        //设置选择值
        String[] showFolders = req.getParameterValues("showFolder");
        if (showFolders != null && showFolders.length > 0) {
            clientConfig.setShowFolderList(List.of(showFolders));
        }
        PageConfigUtil.saveClientConfig(req, resp, clientConfig);
        return "redirect:/userConfig/toBrowserSet.do";
    }

    /**
     * 发送动态密码
     */
    @RequestMapping(value = "viewPass.do")
    public void viewPass(HttpServletRequest req, HttpServletResponse resp, ModelMap context) {
        FetchSend ajaxSend = () -> {
            return ExceptionUtil.wrapperRuntimeR(() -> {
                LoginUser loginUser = loginService.getLoginUserByCookieCheckedSession(req);
                if (null == loginUser) {
                    FlyWebUtil.sendRedirectMsg(resp, "guest can't config info");
                    return null;
                }

                //加载用户全局配置信息，所有浏览器都有效
                UserConfig userConfig = ExceptionUtil.wrapperRuntimeR(() -> userService.getUserinfo(loginUser.pin()).getUserConfig());
                mailUtil.sendMail("查看密码：", "(" + userConfig.getMima() + ")", null, userConfig.getEmail());
                return "已经发送邮箱";
            });
        };
        ajaxSend.doAjax(req, resp);
    }


    /**
     * 异步写是否全屏幕的cookies
     */
    @RequestMapping(value = "writeFullScreenReverseCookies.do")
    public void writeFullScreenReverseCookies(HttpServletRequest req,
                                              HttpServletResponse resp, ModelMap context) throws RepositoryException, IOException {
        //权限判定
        FlyContext flyContext = flyContextFacade.getFlyContext(req, resp);
        if (null == flyContext.loginUser()) {
            FlyWebUtil.sendRedirectMsg(resp, "guest can't toAddMdArticle");
            return;
        }
        PrintWriter writer = null;
        resp.setCharacterEncoding("utf-8");
        resp.setContentType("text/html; charset=utf-8");
        boolean ajax = "XMLHttpRequest".equals(req.getHeader("X-Requested-With"));
        if (!ajax) {
            return;
        }
        try {
            writer = resp.getWriter();
        } catch (Exception e) {
        }

        ClientConfig clientConfig = flyContext.clientConfig();
        clientConfig.setFullScreen(clientConfig.isFullScreenReverse());
        PageConfigUtil.saveClientConfig(req, resp, clientConfig);

        writer.write("" + clientConfig.isFullScreen());
        writer.close();

    }

    public void setMailUtil(MailUtil2 mailUtil) {
        this.mailUtil = mailUtil;
    }
}
