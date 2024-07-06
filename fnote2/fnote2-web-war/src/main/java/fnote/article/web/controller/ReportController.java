package fnote.article.web.controller;

import fly4j.common.util.RepositoryException;
import flynote.application.analysis.SiteStatisticService;
import fnote.domain.config.FlyContext;
import fnote.domain.config.FlyContextFacade;
import fnote.web.common.FlyWebUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 博客信息汇总管理
 *
 * @author qryc
 */
@Controller
@RequestMapping("/report")
public class ReportController {
    private SiteStatisticService siteStatisticService;
    private FlyContextFacade flyContextFacade;

    /**
     * 备份博客页面 直接使用备份地址生成下载路径 生成备份文件 @see IkonwJob
     */
    @RequestMapping(value = "viewReport.do")
    public String viewReport(HttpServletRequest req, HttpServletResponse resp,
                             ModelMap modelMap) throws RepositoryException, IOException {
        FlyContext flyContext = flyContextFacade.getFlyContext(req, resp);

        //只有管理员自己才可以备份博客，如果是浏览者或者未登录，不可访问
        if (null == flyContext.loginUser()) {
            FlyWebUtil.sendRedirectMsg(resp, "guest can't backBlogs");
            return null;
        }
        // 加入站点文件数量统计信息
        Long start=System.currentTimeMillis();
        modelMap.put("siteStatistic", siteStatisticService.getSiteStatistic(flyContext.getPin()));
        System.out.println("siteStatistic"+(System.currentTimeMillis()-start));

        start=System.currentTimeMillis();
        modelMap.put("articleSizeGram", siteStatisticService.getArticleSizeGram(flyContext.getPin()));
        System.out.println("articleSizeGram"+(System.currentTimeMillis()-start));

        start=System.currentTimeMillis();
        modelMap.put("articleYearGram", siteStatisticService.getArticleYearCreateGram(flyContext.getPin()));
        System.out.println("articleYearGram"+(System.currentTimeMillis()-start));

        start=System.currentTimeMillis();
        modelMap.put("articleYearModifyGram", siteStatisticService.getArticleYearModifyGram(flyContext.getPin()));
        System.out.println("articleYearModifyGram"+(System.currentTimeMillis()-start));

        start=System.currentTimeMillis();
        modelMap.put("articleLengthGram", siteStatisticService.getArticleLengthGram(flyContext, flyContext.getEncryptPwd()));
        System.out.println("articleLengthGram"+(System.currentTimeMillis()-start));

        start=System.currentTimeMillis();
        modelMap.put("maturityGram", siteStatisticService.getArticleMaturityGram(flyContext, flyContext.getEncryptPwd()));
        System.out.println("maturityGram"+(System.currentTimeMillis()-start));
        return "siteStatistic/report";
    }

    public void setSiteStatisticService(SiteStatisticService siteStatisticService) {
        this.siteStatisticService = siteStatisticService;
    }

    public void setFlyContextFacade(FlyContextFacade flyContextFacade) {
        this.flyContextFacade = flyContextFacade;
    }
}
