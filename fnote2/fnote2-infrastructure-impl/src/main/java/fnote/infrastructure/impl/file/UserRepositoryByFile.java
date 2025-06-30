package fnote.infrastructure.impl.file;

import fly4j.common.crypto.AESUtil;
import fly4j.common.util.JsonUtils;
import fly4j.common.util.RepositoryException;
import fnote.common.PathService;
import fnote.user.domain.entity.BaseDomain;
import fnote.user.domain.entity.IUserInfo;
import fnote.user.domain.entity.UserConfig;
import fnote.user.domain.entity.UserInfo;
import fnote.user.domain.infrastructure.UserRepository;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class UserRepositoryByFile implements UserRepository {
    static final Logger log = LoggerFactory.getLogger(UserRepositoryByFile.class);

    private String userInfoCryptPwd;
    //用户密码，默认用户文章加密密码
    private String defaultUserArticlePwd;
    private PathService pathService;

    private Path getUserinfoFilePath(String pin) {
        return pathService.getConfigDir().resolve("userinfo").resolve("user_" + pin + ".gwf");
    }

    @Override
    public void insertUserInfo(IUserInfo userInfo) throws RepositoryException {
        RepositoryException.wrapper(() -> {
            if (userInfo instanceof UserInfo flyUserInfo) {
                var filePath = this.getUserinfoFilePath(flyUserInfo.getPin());
                var json = JsonUtils.writeValueAsString(new UserInfoDo(flyUserInfo, userInfoCryptPwd));
                if (Files.notExists(filePath.getParent()))
                    Files.createDirectories(filePath.getParent());
                Files.writeString(filePath, json);
            }
        });
    }

    @Override
    public void updateUserInfo(IUserInfo userInfo) throws RepositoryException {
        insertUserInfo(userInfo);
    }

    @Override
    public IUserInfo getUserinfo(String pin) throws RepositoryException {
        return RepositoryException.wrapperR(() -> {
            Path path = this.getUserinfoFilePath(pin);
            log.info("getUserinfo:"+path.toString());
            if (Files.notExists(path)) {
                return null;
            }
            var json = Files.readString(path);
            var infoDo = JsonUtils.readValue(json, UserInfoDo.class);
            log.info("infoDo:"+infoDo);
            return null == infoDo ? null : infoDo.buildBo(userInfoCryptPwd);
        });
    }


    @Override
    public void delUser(String pin) throws RepositoryException {
        RepositoryException.wrapper(() -> {
            FileUtils.deleteDirectory(this.getUserinfoFilePath(pin).toFile());
        });

    }


    @Override
    public List<IUserInfo> findAllUserInfo() throws RepositoryException {
        List<IUserInfo> userInfos = new ArrayList<IUserInfo>();
        File file = pathService.getRootDir().toFile();
        for (File cFile : file.listFiles()) {
            boolean isUserDir = !cFile.getName().startsWith(".")
                    && cFile.isDirectory();
            log.info(cFile.getAbsolutePath() + " " + isUserDir);
            if (isUserDir) {
                Optional.ofNullable(getUserinfo(cFile.getName())).ifPresent(iUserInfo -> userInfos.add(iUserInfo));
            }
        }
        return userInfos;
    }


    /**
     * Created by qryc on 2020/2/4.
     */
    public static class UserInfoDo extends BaseDomain {
        // 密码
        private String password;
        private Map<String, String> extMap = new HashMap<>();

        public UserInfoDo() {
        }

        public UserInfoDo(UserInfo flyUserInfo, String userInfoCryptPwd) {
            //copy
            flyUserInfo.copyTo(this);
            Map<String, String> extMap = flyUserInfo.getExtMap();
            String userConfigJsonStr = JsonUtils.writeValueAsString(flyUserInfo.getUserConfig());
            try {
                String userConfigJsonStrEnrypt = AESUtil.encryptStr2HexStr(userConfigJsonStr, userInfoCryptPwd);
                extMap.put("config", userConfigJsonStrEnrypt);

            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException();
            }
            this.setExtMap(extMap);
            this.setPassword(flyUserInfo.getPassword());

        }

        public UserInfo buildBo(String userInfoCryptPwd) {
            var flyUserInfo = new UserInfo();
            this.copyTo(flyUserInfo);
            flyUserInfo.setPassword(this.getPassword());

            if (null != this.extMap) {
                /**
                 * 解密用户配置
                 */
                String miConfig = this.getExtMap().get("config");
                if (!StringUtils.isBlank(miConfig)) {
                    this.extMap.remove("config");
                    try {
                        String config = AESUtil.decryptHexStrToStr(miConfig, userInfoCryptPwd);
                        UserConfig userConfig = JsonUtils.readValue(config, UserConfig.class);
                        flyUserInfo.setUserConfig(userConfig);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }

                flyUserInfo.setExtMap(this.extMap);
            }

            return flyUserInfo;
        }


        public String getPassword() {
            return password;
        }

        public UserInfoDo setPassword(String password) {
            this.password = password;
            return this;
        }


        public Map<String, String> getExtMap() {
            return extMap;
        }

        public UserInfoDo setExtMap(Map<String, String> extMap) {
            this.extMap = extMap;
            return this;
        }
    }

    public void setDefaultUserArticlePwd(String defaultUserArticlePwd) {
        this.defaultUserArticlePwd = defaultUserArticlePwd;
    }

    @Override
    public String getDefaultUserArticlePwd() {
        return defaultUserArticlePwd;
    }

    @Override
    public void createUserDirs(String pin) throws IOException {
        //创建用户的数据目录
        if (Files.notExists(pathService.getUserDir(pin)))
            Files.createDirectories(pathService.getUserDir(pin));
//        //创建用户备份路径目录
//        if (Files.notExists(FlyConfig.getUserDir4Back(pin)))
//            Files.createDirectories(FlyConfig.getUserDir4Back(pin));
    }

    public void setUserInfoCryptPwd(String userInfoCryptPwd) {
        this.userInfoCryptPwd = userInfoCryptPwd;
    }


    public void setPathService(PathService pathService) {
        this.pathService = pathService;
    }

}
