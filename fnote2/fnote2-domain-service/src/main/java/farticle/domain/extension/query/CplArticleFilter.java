
package farticle.domain.extension.query;

import farticle.domain.entity.CplArticle;
import fly4j.common.util.RepositoryException;

import java.util.List;

/**
 * Created by qryc on 2019/12/1.
 */
public interface CplArticleFilter {
    CplArticle filter(CplArticle cplArticle, ArticleQueryParam param) throws RepositoryException;
}
