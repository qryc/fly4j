package fnote.article.maintain;

import farticle.domain.entity.*;
import fly4j.common.util.RepositoryException;
import fnote.user.domain.entity.IdPin;

/**
 * 此处的标准：不可分割的领域操作认为是领域服务，在此基础上添加的权限认证等认为是应用服务，如果应用服务过于简单，会直接写到Conrler
 * Created by qryc on 2019/3/2.
 */
public interface ArticleMaintain {
    CplArticle addOrEditMdArticle(MdArticleParam mdArticleParam) throws RepositoryException;

    CplArticle insertRichArticle(RichArticleParam richArticleParam) throws RepositoryException;

    void appendFrontOfContent(IdPin idPin, String appendStr, String encryptPwd) throws RepositoryException;

    CplArticle insertMdArticle(MdArticleParam mdArticleParam) throws RepositoryException;

    CplArticle updateMdArticle(MdArticleParam mdArticleParam) throws RepositoryException;

    CplArticle updateArticleOrganize(IdPin idPin, ArticleOrganizeParam organize) throws RepositoryException;

    CplArticle updateRichArticle(RichArticleParam richArticleParam, WorkSpaceParam workSpaceParam) throws RepositoryException;

    void deleteArticleById(IdPin idPin, WorkSpaceParam workSpaceParam) throws RepositoryException;

    //业务层知道是否加密，持久化层不知道
    CplArticle getCplArticleDecrypt(IdPin idPin, String encryptPwd) throws RepositoryException;


}
