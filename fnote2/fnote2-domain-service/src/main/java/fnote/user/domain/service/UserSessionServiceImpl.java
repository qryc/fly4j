package fnote.user.domain.service;

import fly4j.common.cache.FlyCache;
import fnote.user.domain.entity.LoginUser;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 集中式session管理
 * Created by qryc on 2015/9/4.
 */
public class UserSessionServiceImpl implements UserSessionService {
    private boolean useSession = true;

    /**
     * key sid value登录的用户
     */
    private FlyCache<String> userSessionCache_sid;

    @Override
    public synchronized LoginUser addUserSessionRSid(String pin) {
        var loginUser = new LoginUser(pin, genSid());
        userSessionCache_sid.put(getSidKey(loginUser), pin, TimeUnit.DAYS.toMillis(60));
        return loginUser;
    }


    @Override
    public synchronized void clear() {
        userSessionCache_sid.invalidateAll();
    }

    @Override
    public boolean verifyLoginBySession(LoginUser loginUser) {
        if (!useSession) {
            return true;
        } else {
            var spin = (String) userSessionCache_sid.get(getSidKey(loginUser));
            return null != spin && spin.equals(loginUser.pin());
        }
    }

    @Override
    public Map<String, String> getUserSessions() {
        var map = new HashMap<String, String>();
        userSessionCache_sid.asMap().forEach((key, value) -> {
            map.put(key, value);
        });
        return map;
    }

    private String genSid() {
        return RandomStringUtils.random(10, true, false);
    }

    public String getSidKey(LoginUser loginUser) {
        return loginUser.pin() + "-" + loginUser.sid();
    }

    public void setUseSession(boolean useSession) {
        this.useSession = useSession;
    }

    public void setUserSessionCache_sid(FlyCache userSessionCache_sid) {
        this.userSessionCache_sid = userSessionCache_sid;
    }
}
