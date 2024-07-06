package fnote.user.domain.service;

import fly4j.common.util.RepositoryException;
import fnote.user.domain.entity.UserConfig;
import fnote.user.domain.entity.UserInfo;
import fnote.user.domain.infrastructure.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

/**
 * Created by qryc on 2020/2/5.
 */
public class UserServiceImpl implements UserService {
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    protected UserRepository userRepository;
    private String defaultUserArticlePwd;


    @Override
    public void regUser(String pin, String email) throws RepositoryException, IOException {
        if (userRepository.getUserinfo(pin) != null) {
            throw new RuntimeException("user is exist,can't reg for" + pin);
        }
        userRepository.createUserDirs(pin);


        UserInfo flyUserInfo = new UserInfo();
        flyUserInfo.setPin(pin);

        //保存用户配置
        var userConfig = UserConfig.getDefaultUserConfig(userRepository.getDefaultUserArticlePwd());
        userConfig.setEmail(email);
        flyUserInfo.setUserConfig(userConfig);

        userRepository.insertUserInfo(flyUserInfo);


    }


    @Override
    public void updateEmail(String pin, String email) throws RepositoryException {
        var userInfo = (UserInfo) userRepository.getUserinfo(pin);
        userInfo.getUserConfig().setEmail(email);
        userRepository.updateUserInfo(userInfo);
    }

    @Override
    public void updateUserConfig(String pin, UserConfig userConfig, Map<String, String> extMap) throws RepositoryException {
        var userInfo = (UserInfo) userRepository.getUserinfo(pin);
        userInfo.getUserConfig().setHomePage(userConfig.getHomePage());
        userInfo.getUserConfig().setTitle(userConfig.getTitle());
        userInfo.getUserConfig().setMima(userConfig.getMima());
        userInfo.getUserConfig().setMaturity(userConfig.getMaturity());
        userInfo.getUserConfig().setAutoSave(userConfig.isAutoSave());
        userInfo.getExtMap().putAll(extMap);
        userRepository.updateUserInfo(userInfo);
    }

    @Override
    public void updateExtMapConfig(String pin, Map<String, String> extMap) throws RepositoryException {
        var userInfo = (UserInfo) userRepository.getUserinfo(pin);
        extMap.forEach((k, v) -> {
            userInfo.getExtMap().put(k, v);
        });
        userRepository.updateUserInfo(userInfo);
    }

    @Override
    public void updateUserCryptPass(String pin, String CryptPass) throws RepositoryException {
        var userInfo = (UserInfo) this.getUserinfo(pin);
        userInfo.getUserConfig().setMima(CryptPass);
        userRepository.updateUserInfo(userInfo);
    }

    @Override
    public UserInfo getUserinfo(String pin) throws RepositoryException {
        UserInfo userInfo = (UserInfo) userRepository.getUserinfo(pin);
        return userInfo;
    }

    @Override
    public UserConfig getUserConfig(String pin) throws RepositoryException {
        var flyUserInfo = (UserInfo) userRepository.getUserinfo(pin);
        var userConfig = flyUserInfo.getUserConfig();
        return (null != userConfig) ? userConfig : UserConfig.getDefaultUserConfig(defaultUserArticlePwd);
    }


    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void setDefaultUserArticlePwd(String defaultUserArticlePwd) {
        this.defaultUserArticlePwd = defaultUserArticlePwd;
    }
}
