package fnote.user.domain.entity;

import java.util.HashMap;
import java.util.Map;

public class UserInfo extends BaseDomain<UserInfo> implements IUserInfo {
    public final static String ADMIN_PIN = "admin";//管理员
    /**
     * 用户配置项，不写在Userinfo中，Userinfo不限制Key,由使用者决定
     * 现用户不独立赋能，先写在UserInfo中
     */
    public static final String EXT_ZIPPWD = "zipPwd";
    public static final String EXT_AJAXSAVE = "ajaxSave";

    // 密码
    private String password;
    //加密存儲用戶配置
    private UserConfig userConfig;
    //自定义用户配置
    private Map<String, String> extMap = new HashMap<>();

    public UserConfig getUserConfig() {
        return userConfig;
    }

    public void setUserConfig(UserConfig userConfig) {
        this.userConfig = userConfig;
    }

    public Map<String, String> getExtMap() {
        return extMap;
    }

    public String getExtMapValue(String key) {
        return extMap.get(key);
    }

    public void setExtMap(Map<String, String> extMap) {
        this.extMap = extMap;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    public UserInfo setPassword(String password) {
        this.password = password;
        return this;
    }

    @Override
    public String getEmail() {
        return userConfig.getEmail();
    }


}
