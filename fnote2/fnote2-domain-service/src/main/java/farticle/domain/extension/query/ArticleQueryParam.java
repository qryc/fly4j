package farticle.domain.extension.query;


import fnote.domain.config.FlyContext;

/**
 * Created by IntelliJ IDEA.
 * UserInfo: qryc
 * Date: 14-2-2
 * Time: 下午1:49
 * To change this template use File | Settings | File Templates.
 */
public class ArticleQueryParam {
    private String rootPath;
    private String searchTitle;
    //业务标识
    private String buId;
    private Integer showMaturity;

    private FlyContext flyContext;

    public ArticleQueryParam(String rootPath, String searchTitle, String buId, Integer showMaturity, FlyContext flyContext) {
        this.rootPath = rootPath;
        this.searchTitle = searchTitle;
        this.buId = buId;
        this.showMaturity = showMaturity;
        this.flyContext = flyContext;
    }

    public String getRootPath() {
        return rootPath;
    }

    public String getPin() {
        return flyContext.getPin();
    }


    public String getSearchTitle() {
        return searchTitle;
    }


    public String getBuId() {
        return buId;
    }


    public Integer getShowMaturity() {
        return showMaturity;
    }
    public String getEncryptPwd() {
        return flyContext.getEncryptPwd();
    }

    public FlyContext getFlyContext() {
        return flyContext;
    }
}
