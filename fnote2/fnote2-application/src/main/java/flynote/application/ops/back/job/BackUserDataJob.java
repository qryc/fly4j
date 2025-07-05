package flynote.application.ops.back.job;

import fly.application.git.GitService;
import fly4j.common.cache.FlyCache;
import fly4j.common.util.DateUtil;
import fly4j.common.util.EatExceptionRunnable;
import fly4j.common.util.RepositoryException;
import flynote.application.ops.back.UserDataBackService;
import fnote.common.LogUtil;
import fnote.domain.config.LogConst;
import fnote.user.domain.infrastructure.UserRepository;
import fnote.user.domain.service.UserService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * 定时任务 1.生成博客数量汇总信息 2.生成备份文件
 *
 * @author qryc
 */
public class BackUserDataJob {
    private static final Logger log = LoggerFactory.getLogger(BackUserDataJob.class);
    private static final Logger backLogger = LoggerFactory.getLogger(LogConst.FILE_BACK);
    private boolean openBack = false;
    private boolean emailBack = false;
    private int backSiteHour = 18;
    private UserDataBackService userDataBackService;
    private UserRepository userRepository;
    private FlyCache backJobCache;

    public void startJob() {

        new Thread((EatExceptionRunnable) () -> {
            while (openBack) {
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                LogUtil.out("BackUserDataJob", "startJob", 50);
                log.info("BackUserDataJob-startJob");
                try {
                    GitService.pullAndCommitGit("startJob");
                } catch (Exception e) {
                    log.error("startJob GitService.pullAll", e);
                }
                //如果到了备份小时，并且本小时未备份过，执行备份

                if (emailBack && DateUtil.getCurrHour() == backSiteHour && null == backJobCache.get("backAllUserData")) {
                    backLogger.info("backAllUserData start");
                    backJobCache.put("backAllUserData", System.currentTimeMillis(), TimeUnit.HOURS.toMillis(2));
                    try {
                        this.backAllUserData();
                    } catch (RepositoryException e) {
                        e.printStackTrace();
                    }
                    backLogger.info("backAllUserData end");
                }
            }
        }).start();
    }


    /**
     * 备份博客站点
     * 1.生成备份文件
     * 2.发送邮件
     */
    public synchronized void backAllUserData() throws RepositoryException {
        userRepository.findAllUserInfo().forEach(userInfo -> {
            if (UserService.isAdmin(userInfo.getPin())) {
                backLogger.info("backOneUserData not back " + userInfo.getPin());
                return;
            }
            try {
                backLogger.info("backOneUserData start " + userInfo.getPin());
                //备份用户博客数据
                userDataBackService.backSiteAndSendEmail(userInfo.getPin());
                backLogger.info("backOneUserData end " + userInfo.getPin());
            } catch (Exception e) {
                log.error("backOneUserData exception " + userInfo.getPin() + e, e);
            }
        });
    }

    public void setOpenBack(boolean openBack) {
        this.openBack = openBack;
    }

    public void setBackSiteHour(int backSiteHour) {
        this.backSiteHour = backSiteHour;
    }

    public void setUserDataBackService(UserDataBackService userDataBackService) {
        this.userDataBackService = userDataBackService;
    }

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void setBackJobCache(FlyCache backJobCache) {
        this.backJobCache = backJobCache;
    }

}
