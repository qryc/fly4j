package fnote.user.domain.service;

import fly4j.common.util.RandomUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 动态密码服务类
 * Created by qryc on 2015/12/5.
 */
public class DynPwdService {
    private final Map<String, DynPwd> dynPwdMap_pin = new HashMap<String, DynPwd>();
    //2 minute
    private static final long dynPwdOverTime = 2 * 60 * 1000;

    public String addDynPwd(String pin) {
        String pwd = RandomUtil.random(4, false, true);
        dynPwdMap_pin.put(pin, new DynPwd(pwd));
        return pwd;
    }

    public boolean isPwdOk(String pin, String inputPwd) {
        DynPwd dynPwd = dynPwdMap_pin.get(pin);
        if (null == dynPwd) {
            return false;
        }
        if (Math.abs(System.currentTimeMillis() - dynPwd.createTime) > dynPwdOverTime) {
            return false;
        }
        return dynPwd.pwd.equals(inputPwd);
    }


    static class DynPwd {
        public final String pwd;
        public final long createTime;

        public DynPwd(String pwd) {
            this.pwd = pwd;
            this.createTime = System.currentTimeMillis();
        }
    }

    public static void main(String[] args) {
        DynPwdService dynPwdService = new DynPwdService();
        System.out.println(dynPwdService.addDynPwd("pin1"));
    }
}
