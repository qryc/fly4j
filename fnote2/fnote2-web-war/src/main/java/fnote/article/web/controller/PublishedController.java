package fnote.article.web.controller;

import fnote.article.share.AuthShareServiceImpl;
import fnote.web.common.FlyWebUtil;
import fly4j.common.http.CookiesUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static fly4j.common.http.CookiesUtil.*;

/**
 * 公开访问的内容，单独抽离
 *
 * @author qryc 2020/06/24
 */
@Controller
@RequestMapping("/published")
public class PublishedController {
    //授权码
    public static final String SHARECODE = "shareCode";
    private AuthShareServiceImpl authShareService;

    /**
     * 通过授权码查看博客
     */
    @RequestMapping(value = "viewShareArticle.do")
    public String viewShareArticle(String shareCode, HttpServletRequest req, HttpServletResponse resp, ModelMap context) throws IOException {
        if (StringUtils.isBlank(shareCode)) {
            FlyWebUtil.sendRedirectMsg(resp, "分享已经过期!!!");
            return null;
        }

        //保证从cookies中得到分享码，可以保证图片也会发分享码
        var shareCodeFromCookie = getCookieValue(req, SHARECODE);
        if (StringUtils.isBlank(shareCodeFromCookie)) {
            //先把共享码写入cookie
            CookiesUtil.addHttpOnlyCookie(req, resp, SHARECODE,
                    shareCode, 300);
            //再跳转重新进来，这样所有的img标签就会都传递共享码
            return "redirect:/published/viewShareArticle.do?shareCode=" + shareCode;
        }
        if (!shareCode.equals(shareCodeFromCookie)) {
            //如果是以不一致的授权码，需要清除cookies
            clearCookie(req, resp, SHARECODE);
            //先把共享码写入cookie
            addHttpOnlyCookie(req, resp, SHARECODE,
                    shareCode, 300);
            //再跳转重新进来，这样所有的img标签就会都传递共享码
            return "redirect:/published/viewShareArticle.do?shareCode=" + shareCode;
        }


        /*** 能走到下边的，必须有cookies，使用cookies中的shareCode */

        /**
         * setp2：根据分享码得到博客
         */
        //根据分享码得到博客Id
        AuthShareServiceImpl.ShareArticleInfo shareBlogIdTtl = authShareService.getShareArticleInfo(shareCodeFromCookie);
        if (null == shareBlogIdTtl) {
            FlyWebUtil.sendRedirectMsg(resp, "分享已经过期!!!");
            return null;
        }

        /**
         * setp3：拼装页面显示元素
         */
        // 构造整体博客显示对象
        context.put("publishedArticle", shareBlogIdTtl.getPublishedArticle());
        context.put("ttlMilliSeconds", shareBlogIdTtl.getTtlMilliSeconds());
        context.put("simplePage", true);

        return "article/viewShareArticle";
    }


    public void setAuthShareService(AuthShareServiceImpl authShareService) {
        this.authShareService = authShareService;
    }

}
