package farticle.domain.extension.impl.query.single;

import farticle.domain.entity.ArticleAuthEnum;
import farticle.domain.entity.CplArticle;
import farticle.domain.extension.query.ArticleQueryParam;
import farticle.domain.extension.query.CplArticleFilter;
import fly4j.common.util.RepositoryException;

import java.util.function.BiFunction;

public class WordSearchFilter implements CplArticleFilter {
    @Override
    public CplArticle filter(CplArticle cplArticle, ArticleQueryParam param) throws RepositoryException {
        if (cplArticle.getArticleContent().title().toLowerCase().contains(param.getSearchTitle().toLowerCase())) {
            cplArticle.getArticleOrganize().setOrderNum(Integer.MAX_VALUE);
            return cplArticle;
        }
        if (!ArticleAuthEnum.ENCRYPT_PRI.equals(cplArticle.getArticleContent().authEnum())
                && cplArticle.getArticleContent().content().toLowerCase().contains(param.getSearchTitle().replaceAll("@", "").toLowerCase())) {
            return cplArticle;
        }
        return null;
    }


}
