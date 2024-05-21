package fnote.user.domain.service;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * ip黑名单管理
 * Created by qryc on 2016/4/16.
 */
public interface BlackIpService {
    /**
     * 添加黑名单ip，计数加一
     *
     * @param ip
     */
    void addBlackIP(String ip);

    /**
     * 清除ip黑名单,新功能不需要手工清除了，会自动过期
     */
    void clearBlackIP();


    /**
     * 判断是否是ip黑名单
     *
     * @param ip
     * @return
     */
    boolean isBlack(String ip);

    /**
     * 得到所有登录失败的ip
     *
     * @return
     */
    Map<String, AtomicLong> getAllLoginFailIps();
}
