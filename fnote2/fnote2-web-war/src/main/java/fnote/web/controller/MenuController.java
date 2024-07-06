package fnote.web.controller;

import fly4j.common.http.WebUtil;
import fly4j.common.util.RepositoryException;
import fnote.domain.config.FlyContext;
import fnote.domain.config.FlyContextFacade;
import fnote.menu.Menu;
import fnote.menu.MenuService;
import fnote.user.domain.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.ModelMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * 博客查看控制器
 *
 * @author qryc
 */
//@Controller
//@RequestMapping("/menu")
public class MenuController {
    static final Logger log = LoggerFactory.getLogger(MenuController.class);
    @Resource
    private FlyContextFacade flyContextFacade;

    @Resource
    private MenuService menuService;
    public void setMenu(HttpServletRequest req, ModelMap context, FlyContext flyContext) {
        context.put("menu", getMenu(req, flyContext));
    }

//    @RequestMapping(value = "menuJson.do")
//    @ResponseBody
    public Menu menuJson(HttpServletRequest req, HttpServletResponse resp, ModelMap context) throws RepositoryException {
        try {
            log.debug("MenuController--menuJson");
            var flyContext = flyContextFacade.getFlyContext(req, resp);
            return this.getMenu(req, flyContext);
        } catch (Exception e) {
            log.error("menuJson Error", e);
            throw new RuntimeException(e);
        }
    }

    public Menu getMenu(HttpServletRequest req, FlyContext flyContext) {
        MenuService.MenuContext menuContext = new MenuService.MenuContext();
        menuContext.setExtStringValue("isLogin", flyContext.isLogin());

        menuContext.setExtStringValue("workPath", flyContext.clientConfig().getWorkPath());

        menuContext.setExtStringValue("isPC", flyContext.requestConfig().isPc());
        menuContext.setExtStringValue("role", UserService.getRole(flyContext.getPin()));

        //读取配置化的变量，比如noteFileStr，放入菜单上下文中
        for (String contextVariable : menuService.getContextVariables()) {
            String pValue = WebUtil.getParameterStr(req, contextVariable);
            if (null != pValue) {
                menuContext.setExtStringValue(contextVariable, URLEncoder.encode(pValue, StandardCharsets.UTF_8));
            } else {
                menuContext.setExtStringValue(contextVariable, null);
            }

        }
        Menu menu = menuService.getMenu(menuContext);
        return menu;
    }

}
