package fnote.user.web.filter;

import farticle.domain.entity.WorkSpaceParam;
import fly4j.common.cache.LimitRate;
import fly4j.common.file.FileUtil;
import fly4j.common.http.WebUtil;
import fly4j.common.util.RepositoryException;
import fnote.article.web.controller.PicCache;
import fnote.domain.config.PageConfigUtil;
import fnote.user.domain.entity.LoginUser;
import fnote.user.domain.service.DeployService;
import fnote.user.domain.service.LoginService;
import fnote.user.domain.service.LoginService.LoginConfig;
import fnote.common.StorePathService;
import fnote.web.common.FlyWebUtil;
import fnote.common.web.SpringContextHolder;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * 登录过滤器
 * https://raw.githubusercontent.com/qryc/pic/master/ddd/image-20220629200023987.png
 * <p>
 * http://localhost:8080/Volumes/HomeWork/doc/pic/fly/FlyDesign/Fly%E5%88%86%E5%B1%82.png
 * request.getRequestURI():/Volumes/HomeWork/doc/pic/fly/FlyDesign/Fly%E5%88%86%E5%B1%82.png
 */
public class NoteLoginFilter implements Filter {

    private final static Log log = LogFactory.getLog(NoteLoginFilter.class);

    private WorkSpaceParam getWorkSpace(HttpServletRequest req, HttpServletResponse resp) {
        return WorkSpaceParam.of(PageConfigUtil.getClientConfig(req, resp).getWorkspace());
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException,
            ServletException {
        LoginService loginService = SpringContextHolder.getBean("loginService");
        var request = (HttpServletRequest) req;
        var response = (HttpServletResponse) res;
        var reqURI = URLDecoder.decode(request.getRequestURI(), StandardCharsets.UTF_8);
        log.debug("doFilter reqURI:" + reqURI);

        if (beforeBeforeFilter(request, response)) return;
        // 得到登录用户，并设置到Request域，可以全局使用
        LoginUser loginUser = loginService.getLoginUserByCookieCheckedSession(request);
        if (null != loginUser) {
            System.out.println(loginUser);
            request.setAttribute("loginUser", loginUser);
            request.setAttribute("pin", loginUser.pin());
        }

        //得到网址
        if (StringUtils.isBlank(request.getRequestURI())) {
            return;
        }


        if (afterFilter(request, response, filterChain, loginUser)) return;


        //如果是登录用户，直接放行,数据权限有通过程序控制
        filterChain.doFilter(request, response);
    }

    protected boolean beforeBeforeFilter(HttpServletRequest request, HttpServletResponse response) throws IOException,
            ServletException {
        DeployService deployService = SpringContextHolder.getBean("deployService");
        //如果未初始化，跳转初始化页面
        try {
            //如果网址未初始化，跳转网址初始化的页面
            if (!deployService.isSiteInit()) {
                if (!request.getRequestURI().startsWith("/deploy/")) {
                    response.sendRedirect("/deploy/toInit.do");
                    return true;
                }
            }
        } catch (RepositoryException e) {
            FlyWebUtil.sendRedirectMsg(response, e.getMessage());
            return true;
        }
        return false;
    }

    protected boolean afterFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain, LoginUser loginUser) throws IOException,
            ServletException {
        //spring-bean
        LoginConfig loginConfig = SpringContextHolder.getBean("loginConfig");
        LimitRate limitRate = SpringContextHolder.getBean("loginLimitRate");

        //限流防刷
        if (limitRate.isHotLimit("visitSite")) {
            FlyWebUtil.sendRedirectMsg(response, "too fast");
            return true;
        }
        var reqURI = URLDecoder.decode(request.getRequestURI(), StandardCharsets.UTF_8);
//        log.info("reqURI:" + reqURI);


        /**不需要权限验证的页面，比如网站入口，直接放行*/
        for (var noNeedLoginUrl : loginConfig.getNeverNoNeedLoginUrls()) {
            if (StringUtils.isNotBlank(noNeedLoginUrl) && reqURI.startsWith(noNeedLoginUrl)) {
                filterChain.doFilter(request, response);
                return true;
            }
        }

        //未登录跳转登录页面
        if (null == loginUser) {
            var currentUrl = getCurrentUrl(request, "utf-8");
            String loginUrlStrBuilder = WebUtil.getDomainUrl(request) +
                    loginConfig.getLoginUrl() +
                    URLEncoder.encode(currentUrl, loginConfig.getCharsetName()) +
                    "&fullScreen=true";
            response.sendRedirect(loginUrlStrBuilder);
            return true;
        }


        if (reqURI.startsWith("/article/pic/")) {
            String rewriteUrl = "/viewFile?relativePath=" + reqURI.replaceAll("/article/", "");
            request.getRequestDispatcher(rewriteUrl).forward(request, response);
            log.info("article-pic-redirect--- " + rewriteUrl);

            return true;
        }
        if (reqURI.startsWith("/articleMaintain/pic/")) {
//            System.out.println(PicCache.rel2abPathMap);
            String rewriteUrl = "/viewFile?absolutePath=" + PicCache.getAbsPath(reqURI.replaceAll("/articleMaintain/", ""));
            request.getRequestDispatcher(rewriteUrl).forward(request, response);
            log.info("articleMaintain-pic-redirect--- " + rewriteUrl);
            return true;
        }
        // 用户挂接Doc，pic 绝对路径
        if (reqURI.startsWith("/") && reqURI.contains("/pic/") && Files.exists(Path.of(reqURI))) {
            String rewriteUrl = "/viewFile?absolutePath=" + reqURI;
            request.getRequestDispatcher(rewriteUrl).forward(request, response);
            log.info("contains-pic-redirect--- " + rewriteUrl);

            return true;
        }
        return false;
    }



    /**
     * 获取当前url
     *
     * @param request
     * @return
     */
    private String getCurrentUrl(HttpServletRequest request, String URIEncoding) {

        var queryString = request.getQueryString();
        List<String> parameterNamesByGet = parameterNamesByGet(queryString);
        //以get方式传递的参数数量
        int paramSizeByGet = parameterNamesByGet.size();
        //全部参数量 = redisGet + post
        int paramTotal = request.getParameterMap().size();

        if (paramTotal == 0 && paramSizeByGet == 0) {
            /**
             *  没有参数的情况
             *  有些queryString中的参数比较特殊用getParameter获取不到，
             *  所以没有参数的判断必须为两者都为0
             */
            return request.getRequestURL().toString();
        }

        if ((paramTotal > 0 && paramTotal == paramSizeByGet) || (paramTotal == 0 && paramSizeByGet > 0)) {
            //1. 参数都是get方式上来的.直接返回
            //2.  有些queryString中的参数比较特殊用getParameter获取不到
            return request.getRequestURL() + "?" + queryString;
        }

        /**
         * 剩下的什么鸟情况都可能有
         */
        StringBuffer url = request.getRequestURL();
        url.append("?");

        if (paramSizeByGet > 0) {
            //有get传上来的参数，先拼上queryString
            url.append(queryString);
        }

        String afterConvert = covertToGet(parameterNamesByGet, request.getParameterMap(), URIEncoding);
        if (afterConvert.trim().length() > 0) {
            //post参数转为get参数，添加到queryString后面
            url.append("&").append(afterConvert);
        }

        return url.toString();
    }

    private static List<String> parameterNamesByGet(String queryString) {

        List<String> parameterNames = new ArrayList<String>();

        if (StringUtils.isNotEmpty(queryString)) {
            String[] params = queryString.split("&");

            for (int i = 0; i < params.length; i++) {
                if (params[i].contains("=")) {
                    parameterNames.add(params[i].split("=")[0]);
                }
            }
        }

        return parameterNames;
    }

    public static String encode(String value, String charset) {
        if (value == null || value.length() == 0) {
            return "";
        }
        try {
            return URLEncoder.encode(value, charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static String decode(String value, String charset) {
        if (value == null || value.length() == 0) {
            return "";
        }
        try {
            return URLDecoder.decode(value, charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 从parameterMap中过滤出 post参数，并转为get方式
     *
     * @param parameterNamesByGet
     * @param parameterMap
     * @return
     */
    private static String covertToGet(Collection<String> parameterNamesByGet, Map<String, String[]> parameterMap, String URIEncoding) {
        if (parameterMap.size() == 0) {
            return "";
        }

        StringBuffer postParams = new StringBuffer();
        Set<String> parameterNameSet = parameterMap.keySet();
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {

            if (!parameterNamesByGet.contains(entry.getKey())) {
                postParams.append(entry.getKey()).append("=").append(encode(entry.getValue()[0], URIEncoding)).append("&");
            }
        }

        //截掉最后一个 &
        return postParams.toString().substring(0, postParams.length() - 1);
    }

    @Override
    public void destroy() {

    }
}
