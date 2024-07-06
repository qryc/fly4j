
package farticle.domain.extension.query;

import farticle.domain.entity.CplArticle;

import java.util.stream.Stream;

/**
 * Created by qryc on 2019/12/1.
 */
public interface CplArticlesFilter {
    Stream<CplArticle> filter(Stream<CplArticle> cplArticles, ArticleQueryParam param);
}
