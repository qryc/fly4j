package farticle.domain.extension.query;

import fnote.domain.config.FlyContext;

public class QueryBuilder {
    private String rootPath;
    private String searchTitle;
    private String buId;
    private Integer showMaturity;
    private FlyContext flyContext;

    public static QueryBuilder newBuilder() {
        return new QueryBuilder();
    }

    public QueryBuilder rootPath(String rootPath) {
        this.rootPath = rootPath;
        return this;
    }

    public QueryBuilder searchTitle(String searchTitle) {
        this.searchTitle = searchTitle;
        return this;
    }

    public QueryBuilder buId(String buId) {
        this.buId = buId;
        return this;
    }

    public QueryBuilder showMaturity(Integer showMaturity) {
        this.showMaturity = showMaturity;
        return this;
    }

    public QueryBuilder flyContext(FlyContext flyContext) {
        this.flyContext = flyContext;
        return this;
    }

    public ArticleQueryParam buildArticleQuery() {
        return new ArticleQueryParam(rootPath, searchTitle, buId, showMaturity, flyContext);
    }
}