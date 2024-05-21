package flynote.article.query;

import farticle.domain.extension.query.ArticleQueryParam;
import fly4j.common.util.RepositoryException;
import fnote.domain.config.FlyContext;
import farticle.domain.view.ArticleView4List;

import java.util.List;

/**
 * Created by qryc on 2019/6/8.
 */
public interface ArticleQuery {
    List<ArticleView4List> queryShortArticleViews(ArticleQueryParam queryParam) throws RepositoryException;


}
