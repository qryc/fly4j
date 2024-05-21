package fnote.user.domain.service;

import fnote.user.domain.entity.LoginUser;
import fnote.user.domain.entity.UserInfo;
import fly4j.common.util.RepositoryException;
import fnote.user.domain.entity.UserConfig;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Map;

/**
 * Created by qryc on 2020/2/5.
 */
public interface UserService {
    void regUser(String pin, String email) throws IOException, RepositoryException;

    void updateEmail(String pin, String email) throws RepositoryException;

    void updateUserConfig(String pin, UserConfig userConfig, Map<String, String> extMap) throws RepositoryException;


    void updateExtMapConfig(String pin, Map<String, String> extMap) throws RepositoryException;

    void updateUserCryptPass(String pin, String CryptPass) throws RepositoryException;

    UserInfo getUserinfo(String pin) throws RepositoryException;

    static boolean isAdmin(String pin) {
        if (StringUtils.isEmpty(pin)) {
            return false;
        }
        return pin.equals("admin");
    }

    static boolean isRole(String pin, LoginUser.Role role) {
        if (isAdmin(pin)) {
            return LoginUser.Role.ADMIN.equals(role);
        } else {
            return LoginUser.Role.USER.equals(role);
        }
    }
    static LoginUser.Role getRole(String pin) {
        if (isAdmin(pin)) {
            return LoginUser.Role.ADMIN;
        } else {
            return LoginUser.Role.USER;
        }
    }
    UserConfig getUserConfig(String pin) throws RepositoryException;
}
