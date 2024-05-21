package fnote.user.domain.service;

import fnote.user.domain.entity.LoginUser;

import java.util.Map;

/**
 * ip黑名单管理
 * Created by qryc on 2016/4/16.
 */
public interface UserSessionService {
    /**
     * 添加黑名单ip，计数加一
     */
    LoginUser addUserSessionRSid(String pin);

    /**
     * 清除ip黑名单,新功能不需要手工清除了，会自动过期
     */
    void clear();


    /**
     * 判断集中式Session
     */
    boolean verifyLoginBySession(LoginUser loginUser);

    /**
     * 得到所有黑名单ip
     *
     * @return
     */
    Map<String, String> getUserSessions();


}
