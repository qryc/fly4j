package fnote.web.controller;

import fly4j.common.http.WebUtil;
import fly4j.common.util.FlyPreconditions;
import fly4j.common.util.RepositoryException;
import fnote.domain.config.FlyContext;
import fnote.domain.config.FlyContextFacade;
import fnote.user.domain.entity.AlterUserParam;
import fnote.user.domain.infrastructure.UserRepository;
import fnote.user.domain.service.LoginService;
import fnote.user.domain.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 用户管理
 *
 * @author qryc
 */
@Controller
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserRepository userRepository;
    @Resource
    private UserService userService;
    @Resource
    private LoginService loginService;
    @Resource
    private FlyContextFacade flyContextFacade;

    @RequestMapping(value = "listUser.do")
    public String listUser(HttpServletRequest req, HttpServletResponse resp, ModelMap context) throws RepositoryException {
        FlyContext flyContext = flyContextFacade.getFlyContext(req, resp);
        FlyPreconditions.checkStateTrue(!flyContext.isAdmin(), "only Admin can manage user");
        context.put("users", userRepository.findAllUserInfo());
        return "fu/user/listUser";
    }

    @RequestMapping(value = "addOrEditUser.do")
    public String addOrEditUser(AlterUserParam userInfoParam, HttpServletRequest req, HttpServletResponse resp, ModelMap context) throws RepositoryException, IOException {
        FlyContext flyContext = flyContextFacade.getFlyContext(req, resp);
        FlyPreconditions.checkStateTrue(!flyContext.isAdmin(), "only Admin can manage user");
        if ("add".equals(WebUtil.getParameterStr(req, "opration"))) {
            userService.regUser(userInfoParam.getPin(), userInfoParam.getEmail());
        } else {
            userService.updateEmail(userInfoParam.getPin(), userInfoParam.getEmail());
        }

        return "redirect:/user/listUser.do";
    }

}
