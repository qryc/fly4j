package fnote.user.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fnote.user.domain.service.UserService;

/**
 * 用户的登录信息，保存到cookie中
 * 用户角色：
 * 1.未登录用户  owner = false
 * 2.code码 可以在一定时间段访问code码对应的博客
 * 3.博客主人，可以看到和管理自己的博客
 * 4.管理员登录用户 不可以看到管管理其它人的博客，只可以进行一些系统设置
 * 没有人可以管理所有人的博客，未经过博客主人同意，所有人都没有办法查看博客内容
 * Created by qryc on 2016/8/6.
 */
public class LoginUser {
    private String pin;
    private String sid;
    public LoginUser() {
    }
    public LoginUser(String pin, String sid) {
        this.pin = pin;
        this.sid = sid;
    }

    @JsonIgnore
    public boolean isAdmin() {
        return UserService.isAdmin(pin);
    }

    @JsonIgnore
    public boolean isRole(Role role) {
        return UserService.isRole(pin, role);
    }


    public enum Role {
        ADMIN, USER
    }
    public String pin() {
        return pin;
    }
    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getSid() {
        return sid;
    }
    public String sid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

}


