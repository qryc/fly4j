package fnote.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 博客分类控制器
 *
 * @author qryc
 */
@Controller
@RequestMapping("/prompt")
public class PromptController {
    private static final Logger log = LoggerFactory.getLogger(PromptController.class);

    /**
     * 显示提示消息
     */
    @RequestMapping(value = "promptMsg")
    public String promptMsg(String msg, HttpServletRequest req, HttpServletResponse resp, ModelMap modelMap) {
        modelMap.put("msg", msg);
        modelMap.put("simplePage", true);
        return "fu/msg";
    }


}
