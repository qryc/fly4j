package farticle.domain.extension.impl.query.view;

import farticle.domain.extension.query.ArticleViewFilter;
import farticle.domain.view.ArticleView;
import fnote.domain.config.FlyConfig;
import fnote.domain.config.FlyContext;

public class LocalHerfFilter implements ArticleViewFilter {
    @Override
    public void filter(ArticleView articleView, FlyContext flyContext) {
        if (!FlyConfig.onLine) {
            articleView.setHtml(articleView.getHtml().replaceAll("fly4j.cn/", "localhost:8080/"));
        }
    }
}
