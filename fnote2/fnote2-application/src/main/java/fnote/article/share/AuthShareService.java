package fnote.article.share;

import farticle.domain.entity.WorkSpaceParam;
import fly4j.common.util.RepositoryException;
import fnote.user.domain.entity.IdPin;

public interface AuthShareService {
    void shareArticle(Long id, String pin, String encryptPwd, WorkSpaceParam workSpaceParam) throws RepositoryException;

    AuthShareServiceImpl.ShareArticleInfo getShareArticleInfo(IdPin idPin);

    AuthShareServiceImpl.ShareArticleInfo getShareArticleInfo(String shareCode);
}
