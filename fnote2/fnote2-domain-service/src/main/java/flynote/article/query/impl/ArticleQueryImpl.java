package flynote.article.query.impl;

import farticle.domain.entity.CplArticle;
import farticle.domain.extension.query.ArticleQueryParam;
import farticle.domain.extension.query.CplArticleFilter;
import farticle.domain.extension.query.CplArticlesFilter;
import farticle.domain.infrastructure.ArticleRepository;
import farticle.domain.view.ArticleView4List;
import fly4j.common.util.RepositoryException;
import flynote.article.query.ArticleQuery;
import fnote.common.StorePathService;
import fnote.domain.config.FlyContext;
import org.apache.commons.lang3.StringUtils;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;
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
    private Map<String, List<CplArticlesFilter>> cplArticlesFilterMap;
    //单个过滤
    private Map<String, List<CplArticleFilter>> cplArticleFilterMap;
    private final int limitSize = 10000;

    private StorePathService pathService;

    @Override
    public List<ArticleView4List> queryShortArticleViews(ArticleQueryParam queryParam) throws RepositoryException {
        //定义查询结果
        final List<CplArticle> articles = new ArrayList<>();

        //计数器
        AtomicInteger count = new AtomicInteger(0);
        //创建回调函数
        Function<CplArticle, CplArticle> function = cplArticle -> {
            if (count.incrementAndGet() < limitSize) {
                //执行单个文章扩展点
                List<CplArticleFilter> filterList = cplArticleFilterMap.get(queryParam.getBuId());
                if (null != filterList) {
                    for (CplArticleFilter filter : filterList) {
                        //集合过滤器，为了整体能编排，使用的集合
                        if (null != cplArticle) {
                            try {
                                cplArticle = filter.filter(cplArticle, queryParam);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }
                if (null != cplArticle) {
                    articles.add(cplArticle);
                }
            }
            return null;
        };

        //遍历路径，执行查询
        for (Path path : getQueryArticlePaths(queryParam)) {
            articleRepository.findCplArticlesByPin(queryParam.getPin(), path, function);
        }

        //执行文章列表扩展点
        Stream<CplArticle> stream = articles.stream();

        List<CplArticlesFilter> filterList = cplArticlesFilterMap.get(queryParam.getBuId());
        if (null != filterList) {
            for (CplArticlesFilter filter : filterList) {
                //集合过滤器，为了整体能编排，使用的集合
                stream = filter.filter(stream, queryParam);
            }
        }

        return stream.map(cplArticle -> new ArticleView4List(cplArticle, queryParam.getFlyContext())).toList();
    }

    private List<Path> getQueryArticlePaths(ArticleQueryParam queryParam) {
        List<Path> paths = new ArrayList<>();
        if (StringUtils.isNotBlank(queryParam.getRootPath())) {
            //指定目录文章
            paths.add(Path.of(queryParam.getRootPath()));
        } else {
            //查询用户默认目录文章
            for (Path path : pathService.getAllArticleDirPaths(queryParam.getFlyContext().getPin(), queryParam.getFlyContext().getLoginUser().getOtherLabel())) {
                paths.add(path);
            }
        }
        return paths;
    }

    public void setPathService(StorePathService pathService) {
        this.pathService = pathService;
    }

    public void setArticleRepository(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public void setCplArticlesFilterMap(Map<String, List<CplArticlesFilter>> cplArticlesFilterMap) {
        this.cplArticlesFilterMap = cplArticlesFilterMap;
    }

    public void setCplArticleFilterMap(Map<String, List<CplArticleFilter>> cplArticleFilterMap) {
        this.cplArticleFilterMap = cplArticleFilterMap;
    }
}
