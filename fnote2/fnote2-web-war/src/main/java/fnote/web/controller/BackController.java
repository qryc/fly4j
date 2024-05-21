package fnote.web.controller;

import fly4j.common.util.EatExceptionRunnable;
import fly4j.common.util.ExceptionUtil;
import fnote.domain.config.FlyContextFacade;
import flynote.application.ops.back.UserDataBackService;
import fly4j.common.util.RepositoryException;
import fnote.web.common.FlyWebUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 博客信息汇总管理
 *
 * @author qryc
 */
@Controller
@RequestMapping("/backAdmin")
public class BackController {
    private static final Logger log = LoggerFactory
            .getLogger(BackController.class);
    @Resource
    private UserDataBackService backService;
    @Resource
    private ThreadPoolTaskExecutor flyExecutor;
    @Resource
    private FlyContextFacade flyContextFacade;

    /**
     * 备份博客页面 直接使用备份地址生成下载路径 生成备份文件 @see IkonwJob
     */
    @RequestMapping(value = "backSite.do")
    public String backSite(boolean gen, HttpServletRequest req, HttpServletResponse resp,
                           ModelMap modelMap) throws RepositoryException, IOException {
        var flyContext = flyContextFacade.getFlyContext(req, resp);

        //只有管理员自己才可以备份博客，如果是浏览者或者未登录，不可访问
        if (null == flyContext.loginUser()) {
            FlyWebUtil.sendRedirectMsg(resp, "guest can't backBlogs");
            return null;
        }

        /**
         *  手工触发备份博客
         */
        if (gen) {
            //时间太长了，改异步
            flyExecutor.execute((EatExceptionRunnable) () -> ExceptionUtil.wrapperRuntime(() -> backService.backSiteAndSendEmail(flyContext.getPin())));
        }


        /**
         * 加入统计结果信息
         */
        // 加入备份文件列表下载链接，具体的压缩包由定时任务产生
        modelMap.put("backDownInfo", backService.getBackupFilesInfo(flyContext.getPin()));


        return "siteStatistic/down";
    }

}
