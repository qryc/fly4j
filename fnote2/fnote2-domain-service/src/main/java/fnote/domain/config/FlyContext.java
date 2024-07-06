package fnote.domain.config;

import fnote.user.domain.entity.LoginUser;
import fnote.user.domain.entity.UserConfig;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 上下文对象
 */
public class FlyContext {
    UserConfig userConfig;
    LoginUser loginUser;
    RequestConfig requestConfig;
    ClientConfig clientConfig;

    private static Map<String, String> rootPathMap = new HashMap<>();

    public FlyContext(UserConfig userConfig, LoginUser loginUser, RequestConfig requestConfig, ClientConfig clientConfig) {
        this.userConfig = userConfig;
        this.loginUser = loginUser;
        this.requestConfig = requestConfig;
        this.clientConfig = clientConfig;
    }

    public static FlyContext getFlyContext4Test(String pin, String mima) {

        //设置内存密码
        var userConfig = new UserConfig();
        userConfig.setMima(mima);

        return new FlyContext(userConfig, new LoginUser(pin, ""), new RequestConfig(), new ClientConfig());
    }

    public static FlyContext getFlyContext4Test(String pin, String mima, ClientConfig clientConfig) {

        //设置内存密码
        var userConfig = new UserConfig();
        userConfig.setMima(mima);

        return new FlyContext(userConfig, new LoginUser(pin, ""), new RequestConfig(), clientConfig);
    }

    public void setCurrentWorkRootPath(String rootPath) {
        FlyContext.rootPathMap.put(this.getPin(), rootPath);
    }

    //    public static void setCurrentWorkRootPath(String pin, String rootPath) {
//        FlyContext.rootPathMap.put(pin, rootPath);
//    }
    public static String getCurrentWorkRootPath(String pin) {
        return FlyContext.rootPathMap.get(pin);
    }

    public String getPin() {
        return loginUser.pin();
    }

    public LoginUser getLoginUser() {
        return loginUser;
    }

    public boolean isMobileSite() {
        return requestConfig.isMobileSite();
    }

    public boolean isLogin() {
        return loginUser != null && StringUtils.isNotBlank(loginUser.pin());
    }

    public boolean isAdmin() {
        return this.loginUser.isAdmin();
    }

    public String getEncryptPwd() {
        return this.userConfig.getMima();
    }

    public ViewConfig getViewConfig() {
        return new ViewConfig();
    }

    public ClientConfig clientConfig() {
        return clientConfig;
    }

    public LoginUser loginUser() {
        return loginUser;
    }

    public RequestConfig requestConfig() {
        return requestConfig;
    }

    public UserConfig userConfig() {
        return userConfig;
    }
}
