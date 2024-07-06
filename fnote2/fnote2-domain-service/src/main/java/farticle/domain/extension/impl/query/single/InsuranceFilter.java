package farticle.domain.extension.impl.query.single;

import farticle.domain.entity.CplArticle;
import farticle.domain.extension.query.ArticleQueryParam;
import farticle.domain.extension.query.CplArticleFilter;
import fly4j.common.util.RepositoryException;

/**
 * 过滤保险柜博客
 */
public class InsuranceFilter implements CplArticleFilter {
    //是否展示保险柜内容
    private boolean showInsurance = false;

    @Override
    public CplArticle filter(CplArticle cplArticle, ArticleQueryParam param) throws RepositoryException {
        var clientConfig = param.getFlyContext().clientConfig();
        //客户短是否查看打开保险柜
        var clientInsurane = clientConfig.isInsurance();
        //如果当前功能允许显示保险柜，并且客户端设置可以查看柜，显示保险内容
        if (showInsurance && clientInsurane) {
            return cplArticle;
        } else {
            //只展示不在保险柜的文章
            if (!cplArticle.getArticleOrganize().isInsurance()) {
                return cplArticle;
            } else {
                return null;
            }
        }
    }


    public void setShowInsurance(boolean showInsurance) {
        this.showInsurance = showInsurance;
    }
}
