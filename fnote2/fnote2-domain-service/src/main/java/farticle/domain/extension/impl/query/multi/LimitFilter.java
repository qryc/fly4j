package farticle.domain.extension.impl.query.multi;

import farticle.domain.entity.CplArticle;
import farticle.domain.extension.query.ArticleQueryParam;
import farticle.domain.extension.query.CplArticlesFilter;

import java.util.stream.Stream;

/**
 * Created by qryc on 2019/12/1.
 */
public class LimitFilter implements CplArticlesFilter {
    private int size = 10000;

    @Override
    public Stream<CplArticle> filter(Stream<CplArticle> cplArticles, ArticleQueryParam param) {
        return cplArticles.limit(size);
    }


    public void setSize(int size) {
        this.size = size;
    }
}
