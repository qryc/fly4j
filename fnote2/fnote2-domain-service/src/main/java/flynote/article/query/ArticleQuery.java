package flynote.article.query;

import farticle.domain.extension.query.ArticleQueryParam;
import farticle.domain.view.ArticleView4List;
import fly4j.common.util.RepositoryException;

import java.util.List;

/**
 * Created by qryc on 2019/6/8.
 */
public interface ArticleQuery {
    List<ArticleView4List> queryShortArticleViews(ArticleQueryParam queryParam) throws RepositoryException;


}
