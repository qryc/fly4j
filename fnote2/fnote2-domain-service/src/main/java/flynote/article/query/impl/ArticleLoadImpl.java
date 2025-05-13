package flynote.article.query.impl;

import farticle.domain.infrastructure.ArticleRepository;
import flynote.article.query.ArticleLoad;
import fnote.common.PathService;
import fnote.domain.config.FlyContext;

import java.nio.file.Path;

/**
 * query blog by html
 * use cache to perform
 * Created by qryc on 2016/3/30.
 */
public class ArticleLoadImpl implements ArticleLoad {
    private ArticleRepository articleRepository;
    //    private List<ArticleLoadFilter> filters;
    private PathService pathService;

    @Override
    public Path getAllArticleDirPaths(FlyContext flyContext) {
        return pathService.getUserDir(flyContext.getPin());
    }


    public void setArticleRepository(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

//    public void setFilters(List<ArticleLoadFilter> filters) {
//        this.filters = filters;
//    }

    public ArticleLoadImpl setPathService(PathService pathService) {
        this.pathService = pathService;
        return this;
    }

    @Override
    public ArticleRepository getArticleRepository() {
        return articleRepository;
    }
}
