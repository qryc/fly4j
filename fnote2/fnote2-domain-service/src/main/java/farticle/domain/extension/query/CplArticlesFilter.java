
package farticle.domain.extension.query;

import farticle.domain.entity.CplArticle;
import farticle.domain.view.ArticleView4List;
import fly4j.common.util.RepositoryException;

import java.util.List;
import java.util.stream.Stream;

/**
 * Created by qryc on 2019/12/1.
 */
public interface CplArticlesFilter {
    Stream<CplArticle> filter(Stream<CplArticle> cplArticles, ArticleQueryParam param);
}
