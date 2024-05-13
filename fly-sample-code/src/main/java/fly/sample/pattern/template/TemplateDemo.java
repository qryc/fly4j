package fly.sample.pattern.template;

/**
 * Created by qryc on 2021/11/1
 */
public class TemplateDemo {
    class Article {
        Long id;
    }

    /**
     * 数据库访问接口
     */
    interface ArticleDao {
        //从数据库根据ID查询文章
        Article getCplArticleById(Long id);
    }

    interface ArticleCache {
        //从缓存根据ID查询文章
        Article getCplArticleById(Long id);

        void cacheArticle(Long id, Article article);
    }

    private static ArticleDao articleDaoMysql;
    private static ArticleCache articleCacheRedis;

    public static void main(String[] args) {
        //dao和cache不应该耦合mysql和redis，此处这样命名为了简化本示例，增加易读性。
        DataLoader<Article> dataLoader = new DataLoader<Article>() {
            @Override
            public Article getObjectFromDb(Long id) {
                //从数据库根据ID查询文章
                return articleDaoMysql.getCplArticleById(id);
            }

            @Override
            public Article getObjectFromCache(Long id) {
                //从缓存根据ID查询文章
                return articleCacheRedis.getCplArticleById(id);
            }

            @Override
            public void setObjectToCache(Long id, Article article) {
                //保存文章到Redis缓存中
                articleCacheRedis.cacheArticle(id, article);
            }
        };
        Long id = 100L;
        Article article = dataLoader.getObject(id);
    }
}
