package farticle.domain.extension.modify;

import farticle.domain.entity.ArticleOrganizeParam;
import farticle.domain.entity.CplArticle;
import fly4j.common.util.RepositoryException;

public interface ArticleOrganizeFilter {
    CplArticle filterOrganize(ArticleOrganizeParam param, CplArticle cplArticle) throws RepositoryException;
}
