package fnote.deploy.service;

import fly4j.common.util.RepositoryException;
import fly4j.common.util.StringConst;
import fnote.common.StorePathService;
import fnote.user.domain.entity.UserInfo;
import fnote.user.domain.infrastructure.UserRepository;
import fnote.user.domain.service.DeployService;
import fnote.user.domain.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;


/**
 * Created by qryc on 2020/2/14.
 */
public class DeployServiceImpl implements DeployService {
    private static final Logger log = LoggerFactory.getLogger(DeployServiceImpl.class);
    private UserRepository userRepository;
    private UserService userService;
    private StorePathService pathService;

    @Override
    public boolean isSiteInit() throws RepositoryException {
        return userRepository.getUserinfo(UserInfo.ADMIN_PIN) != null;
    }

    @Override
    public void installFlySite(String email) throws IOException, RepositoryException {
        if (isSiteInit()) {
            return;
        }

        userService.regUser(UserInfo.ADMIN_PIN, email);
    }


    @Override
    public String getAdminEmail() throws RepositoryException {
        var userInfo = userRepository.getUserinfo(UserInfo.ADMIN_PIN);
        if (null == userInfo) {
            return "";
        }
        return userInfo.getEmail();
    }


    @Override
    public void checkSiteDeploy() {
        log.info(StringConst.getConsoleTitle("fly ENV check"));
        System.out.println(("CHECK OS:" + System.getProperty("os.spaceName")));

        //检查文件系统
        if (Files.exists(pathService.getRootPtah(StorePathService.PATH_ARTICLE))) {
            System.out.println(("CHECK ROOTPATH:" + pathService.getRootPtah(StorePathService.PATH_ARTICLE)) + " exists");
        } else {
            System.out.println(("CHECK ROOTPATH:" + pathService.getRootPtah(StorePathService.PATH_ARTICLE)) + " not exists!!!!");

        }
        log.info(StringConst.getConsoleTitle("fly ENV check end"));

    }

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setPathService(StorePathService pathService) {
        this.pathService = pathService;
    }
}
