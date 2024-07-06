package farticle.domain.extension.impl.query.single;

import farticle.domain.entity.CplArticle;
import farticle.domain.extension.query.ArticleQueryParam;
import farticle.domain.extension.query.CplArticleFilter;
import fly4j.common.util.RepositoryException;

public class DecryptFilter implements CplArticleFilter {
    @Override
    public CplArticle filter(CplArticle cplArticle, ArticleQueryParam param) throws RepositoryException {
        return cplArticle.decryptArticle(param.getEncryptPwd());    }


}
