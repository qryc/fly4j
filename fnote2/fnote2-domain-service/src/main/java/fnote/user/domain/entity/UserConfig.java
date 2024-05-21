
package fnote.user.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 单用户配置
 * Created by qryc on 2015/8/11.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserConfig {
    private String email;

    //网站标题
    public String title;
    //首页
    public String homePage;
    //是否自动保存
    public boolean autoSave;
    public String mima;
    //允许查看的成熟度
    public int maturity = 100;

    /**
     * 新创建用户的时候生成默认用户的配置
     */
    public static UserConfig getDefaultUserConfig(String defaultUserArticlePwd) {
        UserConfig defaultUserConfig = new UserConfig();
        defaultUserConfig.homePage = "article/articles.do?buId=lastEdit";
        defaultUserConfig.title = "首页";
        defaultUserConfig.mima = defaultUserArticlePwd;
        defaultUserConfig.autoSave = true;
        return defaultUserConfig;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHomePage() {
        return homePage;
    }

    public void setHomePage(String homePage) {
        this.homePage = homePage;
    }

    public boolean isAutoSave() {
        return autoSave;
    }

    public void setAutoSave(boolean autoSave) {
        this.autoSave = autoSave;
    }

    public String getMima() {
        return mima;
    }

    public void setMima(String mima) {
        this.mima = mima;
    }

    public int getMaturity() {
        return maturity;
    }

    public void setMaturity(int maturity) {
        this.maturity = maturity;
    }
}
