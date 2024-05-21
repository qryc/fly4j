package farticle.domain.extension.impl.query.view;

import farticle.domain.consts.FlyConst;
import farticle.domain.extension.query.ArticleViewFilter;
import farticle.domain.view.ArticleView;
import farticle.domain.view.CommonMdDecorator;
import farticle.domain.view.TreeService;
import fnote.domain.config.FlyConfig;
import fnote.domain.config.FlyContext;
import org.apache.commons.lang3.StringUtils;

public class LocalHerfFilter implements ArticleViewFilter {
    @Override
    public void filter(ArticleView articleView, FlyContext flyContext) {
        if (!FlyConfig.onLine) {
            articleView.setHtml(articleView.getHtml().replaceAll("fly4j.cn/", "localhost:8080/"));
        }
    }
}
