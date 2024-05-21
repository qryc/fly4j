package fnote.article.share;

import fnote.user.domain.entity.IdPin;
import fly4j.common.util.RepositoryException;
import farticle.domain.entity.WorkSpaceParam;

public interface AuthShareService {
    void shareArticle(Long id, String pin, String encryptPwd, WorkSpaceParam workSpaceParam) throws RepositoryException;

    AuthShareServiceImpl.ShareArticleInfo getShareArticleInfo(IdPin idPin);

    AuthShareServiceImpl.ShareArticleInfo getShareArticleInfo(String shareCode);
}
