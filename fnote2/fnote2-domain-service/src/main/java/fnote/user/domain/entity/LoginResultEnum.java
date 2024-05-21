package fnote.user.domain.entity;


/**
 * Created by qryc on 2016/1/8.
 */
public enum LoginResultEnum {
    LOGIN_OK(100, "登录成功"),
    LOGIN_PIN_NOTEXSIT(102, "用户名不存在"),
    LOGIN_PWD_WRONG(103, "密码不对"),
    LOGIN_IP_BLACK(104, "黑名单ip", "用户名不存在或密码错误i！"),
    LOGIN_EXCEPTION(105, "程序异常");

    public LoginUser loginUser;//携带一些登录用户信息
    public int typeID;
    public String msg;
    public String viewMsg;


    LoginResultEnum(int typeID, String msg, String viewMsg) {
        this.typeID = typeID;
        this.msg = msg;
        this.viewMsg = viewMsg;
    }

    LoginResultEnum(int typeID, String msg) {
        this.typeID = typeID;
        this.msg = msg;
        this.viewMsg = msg;
    }



    @Override
    public String toString() {
        return typeID + " " + viewMsg;
    }

    public boolean isSuccess() {
        return LoginResultEnum.LOGIN_OK.equals(this);
    }

}
