package flynote.article.query.impl;

import farticle.domain.entity.CplArticle;
import farticle.domain.extension.query.ArticleQueryParam;
import farticle.domain.extension.query.CplArticleFilter;
import farticle.domain.extension.query.CplArticlesFilter;
import farticle.domain.infrastructure.ArticleRepository;
import farticle.domain.view.ArticleView4List;
import fly4j.common.util.FlyPreconditions;
import fly4j.common.util.RepositoryException;
import flynote.article.query.ArticleQuery;
import fnote.common.StorePathService;
import org.apache.commons.lang3.StringUtils;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * query blog by html
 * use cache to perform
 * Created by qryc on 2016/3/30.
 */
public class ArticleQueryImpl implements ArticleQuery {

    private ArticleRepository articleRepository;
    //列表过滤
    private Map<String, List<CplArticlesFilter>> articleListFilterMap;
    //单个过滤
    private Map<String, List<CplArticleFilter>> singleArticleFilterMap;
    private static final int MAX_ARTICLES = 10000;

    private StorePathService pathService;

    @Override
    public List<ArticleView4List> queryShortArticleViews(ArticleQueryParam queryParam) throws RepositoryException {
        FlyPreconditions.requireNotEmpty(queryParam, "QueryParam must not be null or empty");
        
        //定义查询结果
        final List<CplArticle> filteredArticles = new ArrayList<>();

        //计数器
        AtomicInteger articleCount = new AtomicInteger(0);
        //创建回调函数
        Function<CplArticle, CplArticle> articleProcessor = article -> {
            if (articleCount.incrementAndGet() < MAX_ARTICLES) {
                return filterAndAddArticle(article, queryParam, filteredArticles);
            }
            return null;
        };

        //遍历路径，执行查询
        for (Path path : getQueryArticlePaths(queryParam)) {
            articleRepository.findCplArticlesByPin(queryParam.getPin(), path, articleProcessor);
        }

        return applyArticleListFilters(filteredArticles, queryParam);
    }

    private CplArticle filterAndAddArticle(CplArticle article, ArticleQueryParam queryParam, List<CplArticle> filteredArticles) {
        if (article == null) {
            return null;
        }

        //执行单个文章扩展点
        List<CplArticleFilter> filterList = singleArticleFilterMap.get(queryParam.getBuId());
        if (filterList != null) {
            for (CplArticleFilter filter : filterList) {
                //集合过滤器，为了整体能编排，使用的集合
                try {
                    article = filter.filter(article, queryParam);
                    if (article == null) {
                        return null;
                    }
                } catch (Exception e) {
                    throw new RuntimeException("Error applying article filter", e);
                }
            }
        }

        filteredArticles.add(article);
        return article;
    }

    private List<ArticleView4List> applyArticleListFilters(List<CplArticle> articles, ArticleQueryParam queryParam) {
        //执行文章列表扩展点
        Stream<CplArticle> articleStream = articles.stream();

        List<CplArticlesFilter> filterList = articleListFilterMap.get(queryParam.getBuId());
        if (filterList != null) {
            for (CplArticlesFilter filter : filterList) {
                //集合过滤器，为了整体能编排，使用的集合
                articleStream = filter.filter(articleStream, queryParam);
            }
        }

        return articleStream.map(article -> new ArticleView4List(article, queryParam.getFlyContext())).toList();
    }

    private List<Path> getQueryArticlePaths(ArticleQueryParam queryParam) {
        if (StringUtils.isNotBlank(queryParam.getRootPath())) {
            //指定目录文章
            return List.of(Path.of(queryParam.getRootPath()));
        }
        //查询用户默认目录文章
        return pathService.getAllArticleDirPaths(queryParam.getFlyContext().getPin());
    }

    public void setPathService(StorePathService pathService) {
        this.pathService = pathService;
    }

    public void setArticleRepository(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public void setCplArticlesFilterMap(Map<String, List<CplArticlesFilter>> cplArticlesFilterMap) {
        this.articleListFilterMap = cplArticlesFilterMap;
    }

    public void setCplArticleFilterMap(Map<String, List<CplArticleFilter>> cplArticleFilterMap) {
        this.singleArticleFilterMap = cplArticleFilterMap;
    }
}
