package fnote.web.listener;

import fly.application.git.GitService;
import fly4j.common.util.IpUtil;
import fly4j.common.util.RepositoryException;
import fnote.common.LogUtil;
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
            int size = 50;
            LogUtil.out("NoteBootInfoLogger", "start log", size);
            IpUtil.getLocalIPList().forEach(ip -> LogUtil.out("ip:", ip, size));
            LogUtil.out("profile:", FlyConfig.profile, size);
            LogUtil.out("OnLine:", FlyConfig.onLine, size);
            LogUtil.out("RootDir:", pathService.getRootDir(), size);
            LogUtil.out("ConfigDir:", pathService.getConfigDir(), size);

            try {
                List<IUserInfo> userInfos = userRepository.findAllUserInfo();
                if (CollectionUtils.isEmpty(userInfos)) {
                    LogUtil.out("no user exist:", "config wrong!!!!!", size);
                } else {
                    userInfos.forEach(iUserInfo -> {
                        LogUtil.out(iUserInfo.getPin() + "'UserDir:", pathService.getUserDir(iUserInfo.getPin()), size);
                    });
                }

            } catch (RepositoryException e) {
                throw new RuntimeException(e);
            }
            LogUtil.out("NoteBootInfoLogger", "end log", size);
        }
    }

    public void setPathService(PathService pathService) {
        this.pathService = pathService;
    }

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
