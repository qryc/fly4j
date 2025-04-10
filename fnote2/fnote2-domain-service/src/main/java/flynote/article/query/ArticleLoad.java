package flynote.article.query;

import farticle.domain.entity.CplArticle;
import farticle.domain.extension.impl.query.single.WorkSpaceFilter;
import farticle.domain.infrastructure.ArticleRepository;
import fly4j.common.util.RepositoryException;
import fnote.domain.config.FlyContext;
import fnote.user.domain.entity.IdPin;

import java.nio.file.Path;
import java.util.List;

/**
 * Created by qryc on 2019/6/8.
 */
public interface ArticleLoad {
    default CplArticle getCplArticleDecryptByFilePath(IdPin idPin, String encryptPwd) throws RepositoryException {
        CplArticle cplArticle = getArticleRepository().getCplArticleById4View(idPin);
        cplArticle.decryptArticle(encryptPwd);
        //调用扩展点执行扩展逻辑
//        if (null != filters) {
//            for (ArticleLoadFilter filter : filters) {
//                //集合过滤器，为了整体能编排，使用的集合
//                cplArticle = filter.filterCplArticle(cplArticle, idPin, encryptPwd);
//            }
//        }

        return cplArticle;
    }

    default CplArticle getCplArticle(FlyContext flyContext, String noteFileStr) throws RuntimeException {
        //查询文章信息
        CplArticle cplArticle = null;
        try {
            cplArticle = getArticleRepository().getCplArticleById4View(IdPin.of(flyContext.getPin(), noteFileStr));
        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        }

        //执行工作空间过滤
        String workspace = flyContext.clientConfig().getWorkspace();
        if (WorkSpaceFilter.filter(cplArticle, workspace) == null) {
            return null;
        }
        //解密文章
        cplArticle.decryptArticle(flyContext.getEncryptPwd());
        return cplArticle;
    }

    default CplArticle getCplArticle4ViewById(FlyContext flyContext, long id, String encryptPwd) throws RepositoryException {
        List<Path> paths = this.getAllArticleDirPaths(flyContext);
        CplArticle cplArticle = null;
        for (Path path : paths) {
            cplArticle = getArticleRepository().getCplArticleById4ViewById(flyContext.getPin(), path, id);
            if (null != cplArticle) {
                break;
            }
        }

        if (null == cplArticle) {
            return null;
        }
        cplArticle.decryptArticle(encryptPwd);
        //调用扩展点执行扩展逻辑
//        if (null != filters) {
//            for (ArticleLoadFilter filter : filters) {
//                //集合过滤器，为了整体能编排，使用的集合
//                cplArticle = filter.filterCplArticle(cplArticle, IdPin.of(flyContext.getPin(), cplArticle.getNoteFileStr()), encryptPwd);
//            }
//        }

        return cplArticle;
    }

    Path getAllArticleDirPaths(FlyContext flyContext);


    ArticleRepository getArticleRepository();
}
