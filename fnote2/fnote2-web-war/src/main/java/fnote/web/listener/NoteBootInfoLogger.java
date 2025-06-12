package fnote.web.listener;

import fly.application.git.GitService;
import fly4j.common.util.IpUtil;
import fly4j.common.util.RepositoryException;
import fnote.common.PathService;
import fnote.domain.config.FlyConfig;
import fnote.user.domain.entity.IUserInfo;
import fnote.user.domain.infrastructure.UserRepository;
import fnote.user.listener.BootInfoLogger;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.List;

/**
 * @author qryc 12/19/2020
 */
public class NoteBootInfoLogger extends BootInfoLogger implements ApplicationListener<ContextRefreshedEvent> {
    static final Logger log = LoggerFactory.getLogger(GitService.class);
    private PathService pathService;
    private UserRepository userRepository;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if (contextRefreshedEvent.getApplicationContext().getParent() == null) {//保证只执行一次
            //启动的时候打印程序相关目录
            NoteBootInfoLogger.out("NoteBootInfoLogger:", "", 20);
            IpUtil.getLocalIPList().forEach(ip -> NoteBootInfoLogger.out("ip:", ip, 20));
            NoteBootInfoLogger.out("profile:", FlyConfig.profile, 20);
            NoteBootInfoLogger.out("OnLine:", FlyConfig.onLine, 20);
            NoteBootInfoLogger.out("RootDir:", pathService.getRootDir(), 20);
            NoteBootInfoLogger.out("ConfigDir:", pathService.getConfigDir(), 20);

            try {
                List<IUserInfo> userInfos = userRepository.findAllUserInfo();
                if (CollectionUtils.isEmpty(userInfos)) {
                    NoteBootInfoLogger.out("no user exist:", "config wrong!!!!!", 20);
                }else {
                    userInfos.forEach(iUserInfo -> {
                        NoteBootInfoLogger.out(iUserInfo.getPin() + "'UserDir:", pathService.getUserDir(iUserInfo.getPin()), 20);
                    });
                }

            } catch (RepositoryException e) {
                throw new RuntimeException(e);
            }
            log.info("NoteBootInfoLogger end-info");
        }
    }

    public void setPathService(PathService pathService) {
        this.pathService = pathService;
    }

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
