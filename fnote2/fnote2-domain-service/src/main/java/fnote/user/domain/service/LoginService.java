package fnote.user.domain.service;

import fly4j.common.crypto.AESUtil;
import fly4j.common.http.CookiesUtil;
import fly4j.common.mail.MailUtil2;
import fly4j.common.util.DateUtil;
import fly4j.common.util.JsonUtils;
import fnote.user.domain.entity.IUserInfo;
import fnote.user.domain.entity.LoginResultEnum;
import fnote.user.domain.entity.LoginUser;
import fnote.user.domain.infrastructure.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

/**
 * 用户登录服务
 *
 * @author qryc
 */
public class LoginService {
    protected static final Logger log = LoggerFactory.getLogger(LoginService.class);
    protected LoginConfig loginConfig;
    protected UserRepository userRepository;
    protected DynPwdService dynPwdService;
    protected BlackIpService blackIpService;
    protected UserSessionService userSessionService;
    protected MailUtil2 mailUtil2;
    protected boolean onLine = true;

    /**
     * 登录配置
     *
     * @author qryc
     */
    public static class LoginConfig {
        //登录cookie名字
        private String loginCookie;
        //登录cookie有效期,默认不过期
        private int loginCookieMaxAge;
        //登录密钥
        private String loginAuthKey;
        //登录地址
        private String loginUrl;
        //优先级一：不需要登录直接放行的url
        private Set<String> neverNoNeedLoginUrls;
        private String charsetName;

        public String getLoginCookie() {
            return loginCookie;
        }

        public LoginConfig setLoginCookie(String loginCookie) {
            this.loginCookie = loginCookie;
            return this;
        }

        public int getLoginCookieMaxAge() {
            return loginCookieMaxAge;
        }

        public LoginConfig setLoginCookieMaxAge(int loginCookieMaxAge) {
            this.loginCookieMaxAge = loginCookieMaxAge;
            return this;
        }

        public String getLoginAuthKey() {
            return loginAuthKey;
        }

        public LoginConfig setLoginAuthKey(String loginAuthKey) {
            this.loginAuthKey = loginAuthKey;
            return this;
        }

        public String getLoginUrl() {
            return loginUrl;
        }

        public LoginConfig setLoginUrl(String loginUrl) {
            this.loginUrl = loginUrl;
            return this;
        }

        public Set<String> getNeverNoNeedLoginUrls() {
            return neverNoNeedLoginUrls;
        }

        public LoginConfig setNeverNoNeedLoginUrls(Set<String> neverNoNeedLoginUrls) {
            this.neverNoNeedLoginUrls = neverNoNeedLoginUrls;
            return this;
        }


        public String getCharsetName() {
            return charsetName;
        }

        public LoginConfig setCharsetName(String charsetName) {
            this.charsetName = charsetName;
            return this;
        }
    }


    public LoginResultEnum login(String pin, String password, String ip) throws Exception {

        //登录ip黑名单判断，第三次开始拦截
        if (blackIpService.isBlack(ip)) {
            //记录密码为了记录刷ip传递的密码规律
            log.info("blackIp:" + ip + " pin:" + pin + " pwd:" + password);
            return LoginResultEnum.LOGIN_IP_BLACK;
        }

        // 取出存储的用户名和密码,已改用动态密码，只判断用户名了
        var userinfo = userRepository.getUserinfo(pin);
        // 如果是数据库中用户名不存在，返回用户名不存在
        if (null == userinfo) {
            blackIpService.addBlackIP(ip);
            return LoginResultEnum.LOGIN_PIN_NOTEXSIT;
        }


        //动态密码
        if (!dynPwdService.isPwdOk(pin, password)) {
            blackIpService.addBlackIP(ip);
            return LoginResultEnum.LOGIN_PWD_WRONG;
        }

        //添加集中式session
        var loginUser = userSessionService.addUserSessionRSid(pin);

        LoginResultEnum.LOGIN_OK.loginUser = loginUser;
        return LoginResultEnum.LOGIN_OK;
    }

    public void addDynPwd(String pin, String ip) throws Exception {
        //登录ip黑名单判断，第三次开始拦截
        if (blackIpService.isBlack(ip)) {
            log.info("blackIp:" + ip + " addDynPwd");
            return;
        }

        var userInfo = Objects.requireNonNull(userRepository.getUserinfo(pin));

        var dynPwd = dynPwdService.addDynPwd(pin);
        if (!onLine) {
            System.out.println("Fly动态登录密码:" + dynPwd);
        } else {
            mailUtil2.sendSimpleMail("Fly动态登录密码" + dynPwd, dynPwd + " " + DateUtil.getDateStr(new Date()) + "两分钟有效", userInfo.getEmail());
        }
    }


    public LoginUser getLoginUserByCookieCheckedSession(HttpServletRequest request) {
        try {
            var cookieValue = CookiesUtil.getCookieValue(request, loginConfig.getLoginCookie());
            if (StringUtils.isBlank(cookieValue)) {
                throw new RuntimeException("getCookieValue is blank");
            } else {
                var jsonStr = AESUtil.decryptHexStrToStr(cookieValue, loginConfig.getLoginAuthKey());
                var loginUser = JsonUtils.readValue(jsonStr, LoginUser.class);
                if (userRepository.getUserinfo(loginUser.pin()) == null) {
                    throw new RuntimeException("LoginUser is not in repository, pin:" + loginUser.pin());
                } else {
                    if (userSessionService.verifyLoginBySession(loginUser)) {
                        return loginUser;
                    } else {
                        throw new RuntimeException("LoginUser use session verify is false,pin" + loginUser.pin());
                    }
                }
            }
        } catch (Exception e) {
//            log.error("getLoginPinByCookieAndSession exception ", e);
//            e.printStackTrace();
            return null;
        }

    }

    public void addLoginCookies(HttpServletRequest req, HttpServletResponse resp, LoginResultEnum logionResult) {
        String cookiesStr = AESUtil.encryptStr2HexStr(JsonUtils.writeValueAsString(logionResult.loginUser), loginConfig.getLoginAuthKey());
        CookiesUtil.addHttpOnlyCookie(req, resp, loginConfig.getLoginCookie(),
                cookiesStr, loginConfig.getLoginCookieMaxAge());
    }

    public void loginOut(IUserInfo user, HttpServletRequest req, HttpServletResponse resp) {
        // 退出登录删除cookies
        try {
            CookiesUtil.clearCookie(req, resp, loginConfig.getLoginCookie());
        } catch (Exception e) {
            log.error("--add loginCookie error-pin" + user.getPin(), e);
        }
        log.info("redisDel login cookie:" + loginConfig.getLoginCookie());
    }

    public void setLoginConfig(LoginConfig loginConfig) {
        this.loginConfig = loginConfig;
    }

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void setDynPwdService(DynPwdService dynPwdService) {
        this.dynPwdService = dynPwdService;
    }

    public void setBlackIpService(BlackIpService blackIpService) {
        this.blackIpService = blackIpService;
    }

    public void setUserSessionService(UserSessionService userSessionService) {
        this.userSessionService = userSessionService;
    }

    public void setOnLine(boolean onLine) {
        this.onLine = onLine;
    }

    public void setMailUtil2(MailUtil2 mailUtil2) {
        this.mailUtil2 = mailUtil2;
    }
}
