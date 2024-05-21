package farticle.domain.extension.impl.query.single;

import farticle.domain.entity.ArticleAuthEnum;
import farticle.domain.entity.CplArticle;
import farticle.domain.extension.query.ArticleQueryParam;

import java.util.function.BiFunction;

public class WordSearchFilter2 implements BiFunction<CplArticle, ArticleQueryParam, CplArticle> {

    @Override
    public CplArticle apply(CplArticle cplArticle, ArticleQueryParam param) {
        if (cplArticle.getArticleContent().title().toLowerCase().contains(param.getSearchTitle().toLowerCase())) {
            return cplArticle;
        }
        if (!ArticleAuthEnum.ENCRYPT_PRI.equals(cplArticle.getArticleContent().authEnum()) &&
                cplArticle.getArticleContent().content().toLowerCase().contains(param.getSearchTitle().replaceAll("@", "").toLowerCase())) {
            return cplArticle;
        }
        return null;
    }
}
