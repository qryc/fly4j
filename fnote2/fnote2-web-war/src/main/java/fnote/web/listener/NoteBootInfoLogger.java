package fnote.web.listener;

import fly.application.git.GitService;
import fly4j.common.util.IpUtil;
import fnote.common.DomainPathService;
import fnote.domain.config.FlyConfig;
import fnote.user.listener.BootInfoLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * @author qryc 12/19/2020
 */
public class NoteBootInfoLogger extends BootInfoLogger implements ApplicationListener<ContextRefreshedEvent> {
    static final Logger log = LoggerFactory.getLogger(GitService.class);
    private DomainPathService pathService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if (contextRefreshedEvent.getApplicationContext().getParent() == null) {//保证只执行一次
            //启动的时候打印程序相关目录
            NoteBootInfoLogger.out("NoteBootInfoLogger:", "", 20);
            IpUtil.getLocalIPList().forEach(ip -> NoteBootInfoLogger.out("ip:", ip, 20));
            NoteBootInfoLogger.out("profile:", FlyConfig.profile, 20);
            NoteBootInfoLogger.out("OnLine:", FlyConfig.onLine, 20);
//            NoteBootInfoLogger.out("HomePath:", FlyConfig.getHomePath(), 20);
//            NoteBootInfoLogger.out("TempPath:", FlyConfig.getTempPath(), 20);
//            NoteBootInfoLogger.out("UserDirPath:", FlyConfig.getUserHome4Data("${userName}"), 20);
//            NoteBootInfoLogger.out("BackDataDirPath:", backPathService.getUserDir4Data("${userName}"), 20);
//            NoteBootInfoLogger.out("ArticlesDirPath:", pathService.getUDirPath(StorePathService.PATH_ARTICLE, "${userName}"), 20);
//            NoteBootInfoLogger.out("getCustomDocPath:", pathService.getCustomDocPath(), 20);

//            NoteBootInfoLogger.out("GitDirPath:", pathService.getURootPath(StorePathService.PATH_GIT, "${userName}"), 20);
//            NoteBootInfoLogger.out("ArticleFilePath:", articlePathService.getStoreFilePath(IdPin.of(1L, "${userName}")), 20);
//            try {
//                if (deployConfigRepository.getDeployConfig() != null && deployConfigRepository.getDeployConfig().getSysEmailConfig() != null)
//                    NoteBootInfoLogger.out("emailConfig:", deployConfigRepository.getDeployConfig().getSysEmailConfig().getFromEmail(), 20);
//            } catch (RepositoryException e) {
//                e.printStackTrace();
//            }
            log.info("NoteBootInfoLogger end-info");
        }
    }

    public void setPathService(DomainPathService pathService) {
        this.pathService = pathService;
    }
}
