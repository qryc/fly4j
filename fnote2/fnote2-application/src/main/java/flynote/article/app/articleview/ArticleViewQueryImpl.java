package flynote.article.app.articleview;

import farticle.domain.extension.query.ArticleViewFilter;
import farticle.domain.view.ArticleView;
import farticle.domain.view.ArticleViewQuery;
import fly4j.common.util.RepositoryException;
import flynote.article.query.ArticleLoad;
import fnote.domain.config.FlyContext;
import fnote.user.domain.entity.IdPin;

import java.util.List;

/**
 * Created by qryc on 2021/10/6
 */
public class ArticleViewQueryImpl implements ArticleViewQuery {
    private ArticleLoad articleLoad;
    private List<ArticleViewFilter> filterList;


    @Override
    public ArticleView getArticleViewByIdWithFilter(FlyContext flyContext, Long id) throws RepositoryException {
        var cplArticle = getArticleLoad().getCplArticle4ViewById(flyContext, id, flyContext.getEncryptPwd());
        ArticleView articleView = new ArticleView(cplArticle);
        if (null != getFilterList()) {
            for (var filter : getFilterList()) {
                //集合过滤器，为了整体能编排，使用的集合
                if (null != cplArticle) {
                    filter.filter(articleView, flyContext);
                }
            }
        }

        return articleView;
    }

    public ArticleView getArticleViewWithFilter(IdPin idPin, FlyContext flyContext) throws RepositoryException {
        var cplArticle = getArticleLoad().getCplArticleDecryptByFilePath(idPin, flyContext.getEncryptPwd());
        ArticleView articleView = new ArticleView(cplArticle);
        if (null != getFilterList()) {
            for (var filter : getFilterList()) {
                //集合过滤器，为了整体能编排，使用的集合
                if (null != cplArticle) {
                    filter.filter(articleView, flyContext);
                }
            }
        }

        return articleView;
    }

    public void setArticleLoad(ArticleLoad articleLoad) {
        this.articleLoad = articleLoad;
    }

    public void setFilterList(List<ArticleViewFilter> filterList) {
        this.filterList = filterList;
    }

    public ArticleLoad getArticleLoad() {
        return articleLoad;
    }

    public List<ArticleViewFilter> getFilterList() {
        return filterList;
    }
}
