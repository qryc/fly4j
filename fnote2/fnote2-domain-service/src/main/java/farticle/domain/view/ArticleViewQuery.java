package farticle.domain.view;

import fly4j.common.util.RepositoryException;
import fnote.domain.config.FlyContext;
import fnote.user.domain.entity.IdPin;

public interface ArticleViewQuery {


    ArticleView getArticleViewByIdWithFilter(FlyContext flyContext, Long id) throws RepositoryException;

    ArticleView getArticleViewWithFilter(IdPin idPin, FlyContext flyContext) throws RepositoryException;

}
