package farticle.domain.extension.query;

import farticle.domain.entity.CplArticle;
import fly4j.common.util.RepositoryException;
import fnote.user.domain.entity.IdPin;

public interface ArticleLoadFilter {
    CplArticle filterCplArticle(CplArticle cplArticle, IdPin idPin, String encryptPwd) throws RepositoryException;
}
