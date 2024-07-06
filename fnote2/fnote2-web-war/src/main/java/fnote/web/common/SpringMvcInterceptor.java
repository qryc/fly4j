package fnote.web.common;

import fly4j.common.file.BroserUtil;
import fly4j.common.util.DateUtil;
import fly4j.common.util.RepositoryException;
import fnote.domain.config.FlyContext;
import fnote.domain.config.FlyContextFacade;
import fnote.filebrowser.web.controller.FlyUtil;
import fnote.user.domain.entity.UserConfig;
import fnote.user.domain.entity.UserInfo;
import fnote.user.domain.infrastructure.UserRepository;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * 登录拦截以及一些页面公共功能
 *
 * @author qryc
 */
public class SpringMvcInterceptor extends HandlerInterceptorAdapter {
    private final static Log log = LogFactory.getLog(SpringMvcInterceptor.class);
    @Resource
    private UserRepository userRepository;
    @Resource
    private FlyContextFacade flyContextFacade;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        if (null == modelAndView)
            return;
        setViewTools(modelAndView);
        var flyContext = flyContextFacade.getFlyContext(request, response);
        setPageConfig(modelAndView, flyContext);

        if (null != flyContext.loginUser())
            loadingOftenUseData(modelAndView, flyContext);
    }

    private void loadingOftenUseData(ModelAndView modelAndView, FlyContext flyContext) {
    }

    private void setPageConfig(ModelAndView modelAndView, FlyContext flyContext) throws RepositoryException {

        if (null != flyContext.loginUser()) {
            var userInfo = (UserInfo) userRepository.getUserinfo(flyContext.getPin());
            UserConfig userConfig = null == userInfo ? UserConfig.getDefaultUserConfig(userRepository.getDefaultUserArticlePwd()) : userInfo.getUserConfig();
            modelAndView.addObject("userPageConfig", new UserPageConfig(userConfig.title, userConfig.homePage));
            modelAndView.addObject("userInfo", userInfo);
        }
        modelAndView.addObject("pageConfig", flyContext.requestConfig());
        modelAndView.addObject("clientConfig", flyContext.clientConfig());
        modelAndView.addObject("flyContext", flyContext);
    }

    private void setViewTools(ModelAndView modelAndView) {
        modelAndView.addObject("dateFormatUtils", new DateUtil());
        modelAndView.addObject("stringUtils", new StringUtils());
        modelAndView.addObject("stringEscapeUtils", new StringEscapeUtils());
        modelAndView.addObject("fileUtils", new FileUtils());
        modelAndView.addObject("urlEncoder", new URLEncoderBean());
        modelAndView.addObject("broserUtil", new BroserUtil());
        modelAndView.addObject("FlyUtil", new FlyUtil());
    }

    public record UserPageConfig(String title, String homePage) {
    }

    public static class URLEncoderBean {
        public String encode(String s) {
            if (StringUtils.isEmpty(s)) {
                return s;
            }
            return URLEncoder.encode(s, StandardCharsets.UTF_8);
        }
    }

}
