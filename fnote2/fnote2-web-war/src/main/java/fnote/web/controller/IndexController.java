package fnote.web.controller;

import fnote.domain.config.FlyContext;
import fnote.domain.config.FlyContextFacade;
import fly4j.common.util.RepositoryException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 博客查看控制器
 *
 * @author qryc
 */
@Controller // 用于标识是处理器类
@RequestMapping("/index") // 请求到处理器功能方法的映射规则
public class IndexController extends MenuController {

    private static final Logger log = LoggerFactory.getLogger(IndexController.class);
    @Resource
    private FlyContextFacade flyContextFacade;

    @RequestMapping(value = "index.do")
    public String index(HttpServletRequest req, HttpServletResponse resp, ModelMap modelMap) throws RepositoryException {
        FlyContext flyContext = flyContextFacade.getFlyContext(req, resp);
        String userHomePage = flyContext.userConfig().homePage;
        if (flyContext.isAdmin()) {
            return "redirect:/user/listUser.do";
        }
//        if (WebUtil.isFromPhone(req)) {
//            return "redirect:/blog/174?append=true";
//        }
//        if (StringUtils.isNotBlank(userHomePage)) {
//            return "redirect:/" + userHomePage;
//        }
        setMenu(req, modelMap, flyContext);

        return "redirect:/userConfig/toBrowserSet.do";
    }

    /**
     * 到添加和修改博客
     */
    @RequestMapping(value = "webSiteMap.do")
    public String webSiteMap(HttpServletRequest req,
                             HttpServletResponse resp, ModelMap modelMap) throws RepositoryException {
        FlyContext flyContext = flyContextFacade.getFlyContext(req, resp);
        setMenu(req, modelMap, flyContext);

        return "fu/menu/webSiteMap";
    }

    public void setFlyContextFacade(FlyContextFacade flyContextFacade) {
        this.flyContextFacade = flyContextFacade;
    }
}
