package fnote.web.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.ui.ModelMap;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * web层的一些小功能封装
 *
 * @author xxgw
 */
public class FlyWebUtil {
    public static final String SMVC_REDIRECT_URL = "redirect:/prompt/promptMsg?msg=";

    private static final Log log = LogFactory.getLog(FlyWebUtil.class);

    /**
     * 在redirect前清理，避免带参数
     *
     * @param modelMap
     */
    private static void clearVolicityTools(ModelMap modelMap) {
        modelMap.remove("dateFormatUtils");
        modelMap.remove("stringUtils");
        modelMap.remove("stringEscapeUtils");
        modelMap.remove("iknowsite");
    }

    public static String getSpringRedirect(String url, ModelMap modelMap) {
        FlyWebUtil.clearVolicityTools(modelMap);
        return "redirect:/" + url;
    }


    public static void sendRedirectMsg(HttpServletResponse resp, String msg) throws IOException {
        resp.sendRedirect("/prompt/promptMsg?msg="
                + java.net.URLEncoder.encode(msg, StandardCharsets.UTF_8));
    }

    /**
     * 跳转出错提示页面
     *
     * @param msg
     * @return
     */
    public static String getSendRedirectMsgPageSpringMvc(String msg) {
        return "redirect:/prompt/promptMsg?fullScreen=true&msg=" + java.net.URLEncoder.encode(msg, StandardCharsets.UTF_8);
    }



}
