package farticle.domain.extension.impl.query.multi;

import farticle.domain.entity.CplArticle;
import farticle.domain.extension.query.ArticleQueryParam;
import farticle.domain.extension.query.CplArticlesFilter;

import java.util.stream.Stream;

/**
 * Created by qryc on 2019/12/1.
 */
public class SortByOrdernumFilter implements CplArticlesFilter {

    @Override
    public Stream<CplArticle> filter(Stream<CplArticle> cplArticles, ArticleQueryParam param) {
        return cplArticles.sorted((o1, o2) -> {
            return o2.getArticleOrganize().getOrderNum() - o1.getArticleOrganize().getOrderNum();
        });
    }

}
