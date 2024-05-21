package fnote.deploy.controller;

import fnote.user.domain.service.DeployService;
import fnote.domain.config.FlyContextFacade;
import fnote.domain.config.FlyContext;
import fly4j.common.util.RepositoryException;
import fly4j.common.util.FlyPreconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 部署控制器
 *
 * @author qryc
 */
@Controller
@RequestMapping("/deploy")
public class DeployController {
    private static final Logger log = LoggerFactory.getLogger(DeployController.class);
    @Resource
    private DeployService deployService;
    @Resource
    private FlyContextFacade flyContextFacade;

    @RequestMapping(value = "toInit.do")
    public String toInit(HttpServletRequest req, HttpServletResponse resp, ModelMap context) throws RepositoryException {
        //如果已经初始化要求必须登录
        if (deployService.isSiteInit()) {
            FlyContext flyContext = flyContextFacade.getFlyContext(req, resp);
            if (!flyContext.isLogin())
                return "redirect:/article/articles.do?buId=lastEdit";
        }
        context.put("email", deployService.getAdminEmail());
//        context.put("defaultCryptPwd", FlyConfig.defaultCryptPwd);
        return "fu/deploy/init";
    }

    @RequestMapping(value = "installFlySite.do")
    public String installFlySite(HttpServletRequest req, HttpServletResponse resp, ModelMap context) throws RepositoryException, IOException {
        //如果已经初始化要求必须登录
        if (deployService.isSiteInit()) {
            FlyContext flyContext = flyContextFacade.getFlyContext(req, resp);
            FlyPreconditions.requireNotEmpty(flyContext.loginUser(), "guest can't  installFlySite");
        }
        deployService.installFlySite(req.getParameter("email"));
        return "redirect:/deploy/toInit.do";
    }


}
