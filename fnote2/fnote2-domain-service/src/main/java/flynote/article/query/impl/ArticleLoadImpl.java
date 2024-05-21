package flynote.article.query.impl;

import farticle.domain.entity.CplArticle;
import farticle.domain.infrastructure.ArticleRepository;
import fly4j.common.util.RepositoryException;
import flynote.article.query.ArticleLoad;
import fnote.common.StorePathService;
import fnote.domain.config.FlyContext;

import java.nio.file.Path;
import java.util.List;

/**
 * query blog by html
 * use cache to perform
 * Created by qryc on 2016/3/30.
 */
public class ArticleLoadImpl implements ArticleLoad {
    private ArticleRepository articleRepository;
    //    private List<ArticleLoadFilter> filters;
    private StorePathService pathService;

    @Override
    public List<Path> getAllArticleDirPaths(FlyContext flyContext) {
        return pathService.getAllArticleDirPaths(flyContext.getPin(), flyContext.loginUser().getOtherLabel());
    }


    public void setArticleRepository(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

//    public void setFilters(List<ArticleLoadFilter> filters) {
//        this.filters = filters;
//    }

    public ArticleLoadImpl setPathService(StorePathService pathService) {
        this.pathService = pathService;
        return this;
    }

    @Override
    public ArticleRepository getArticleRepository() {
        return articleRepository;
    }
}
