package flynote.application.ops.back.job;

import fly.application.git.GitService;
import fly4j.common.cache.FlyCache;
import fly4j.common.file.FileUtil;
import fly4j.common.mail.MailUtil2;
import fly4j.common.util.DateUtil;
import fly4j.common.util.EatExceptionRunnable;
import flynote.application.ops.back.UserDataBackService;
import fnote.domain.config.FlyConfig;
import fnote.domain.config.LogConst;
import fnote.user.domain.service.UserService;
import fnote.user.domain.infrastructure.UserRepository;
import fly4j.common.util.RepositoryException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.mail.EmailException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 定时任务 1.生成博客数量汇总信息 2.生成备份文件
 *
 * @author qryc
 */
public class BackUserDataJob {
    private static final Log log = LogFactory.getLog(BackUserDataJob.class);
    private static final Logger backLogger = LoggerFactory.getLogger(LogConst.FILE_BACK);
    private boolean openBack = true;
    private int backSiteHour = 18;
    private UserDataBackService userDataBackService;
    private UserRepository userRepository;
    private FlyCache backJobCache;
    private static String preMd5 = "";
    private String monitorFilePath;
    private MailUtil2 mailUtil2;

    public void startJob() {

        new Thread((EatExceptionRunnable) () -> {
            while (openBack) {
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.debug("-".repeat(10) + "startJob");
                try {
                    GitService.pullAll("startJob");
                } catch (Exception e) {
                    log.error("startJob GitService.pullAll", e);
                }
                //如果到了备份小时，并且本小时未备份过，执行备份
                boolean emailBack = false;
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
                //监控文件变化
                boolean monitorFileChange = false;
                if (monitorFileChange) {
                    try {
                        if (StringUtils.isNoneBlank(monitorFilePath)) {
                            String currentMd5 = FileUtil.getMD5(new File(monitorFilePath));
                            if (!preMd5.equals(currentMd5)) {
                                String content = DateUtil.getDateStr(new Date()) + "文件变化2，文件=" + monitorFilePath + " , MD5=" + currentMd5 + " preMd5=" + preMd5;
                                System.out.println(content);
                                if (FlyConfig.onLine) {
                                    mailUtil2.sendSimpleMail("文件变化", content, "panpan_002@126.com");
                                }
                            }
                            preMd5 = currentMd5;
                        }
                    } catch (EmailException e) {
                        e.printStackTrace();
                    }
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

    public void setMonitorFilePath(String monitorFilePath) {
        this.monitorFilePath = monitorFilePath;
    }

    public void setMailUtil2(MailUtil2 mailUtil2) {
        this.mailUtil2 = mailUtil2;
    }
}
