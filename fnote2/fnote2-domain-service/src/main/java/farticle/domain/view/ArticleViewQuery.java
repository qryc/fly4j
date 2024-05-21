package farticle.domain.view;

import farticle.domain.extension.query.ArticleViewFilter;
import fly4j.common.util.RepositoryException;
import flynote.article.query.ArticleLoad;
import fnote.domain.config.FlyContext;
import fnote.user.domain.entity.IdPin;

import java.util.List;

public interface ArticleViewQuery {


    ArticleView getArticleViewByIdWithFilter(FlyContext flyContext, Long id) throws RepositoryException;

    ArticleView getArticleViewWithFilter(IdPin idPin, FlyContext flyContext) throws RepositoryException;

}
