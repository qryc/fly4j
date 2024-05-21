package farticle.domain.entity;

/**
 * Created by qryc on 2017/10/3.
 */
public enum ArticleAuthEnum {
    OPEN(0),// "公有"
    PRI(1),//"私有"
    ENCRYPT_PUB(2),// "加密"
    ENCRYPT_PRI(3),//自定义加密,页面只显示密文，自己去解密工具解密
    REAL_OPEN(4);//不需要加密


    ArticleAuthEnum(int authCode) {
        this.authCode = authCode;
    }

    private int authCode = 1;


    public static ArticleAuthEnum getInsByAuthCode(int authCode) {
        for (var blogAuthEnum : ArticleAuthEnum.values()) {
            if (blogAuthEnum.authCode == authCode) {
                return blogAuthEnum;
            }
        }
        return null;
    }

    public boolean equalsCode(int code) {
        return this.getAuthCode() == code;
    }

    public int getAuthCode() {
        return authCode;
    }
}
