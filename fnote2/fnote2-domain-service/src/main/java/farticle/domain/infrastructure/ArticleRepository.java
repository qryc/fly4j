package farticle.domain.infrastructure;

import farticle.domain.entity.ArticleFileParam;
import farticle.domain.entity.CplArticle;
import fnote.user.domain.entity.IdPin;
import fly4j.common.util.RepositoryException;
import farticle.domain.entity.WorkSpaceParam;

import java.nio.file.Path;
import java.util.List;
import java.util.function.Function;

public interface ArticleRepository {
    //快速，可以接受一点延迟
    CplArticle getCplArticleById4View(IdPin idPin) throws RepositoryException;

    CplArticle getCplArticleById4ViewById(String pin, Path rootPath, long id) throws RepositoryException;

    void deleteCplArticleById(IdPin idPin) throws RepositoryException;

    List<CplArticle> findCplArticlesByPin(String pin, Path rootPath, Function<CplArticle, CplArticle> function) throws RepositoryException;

    String insertCplArticle(CplArticle cplArticle) throws RepositoryException;

    void updateCplArticle(CplArticle cplArticle) throws RepositoryException;

    //准确
    CplArticle getCplArticleById4Edit(IdPin idPin) throws RepositoryException;


}
