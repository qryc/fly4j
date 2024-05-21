package farticle.domain.extension.impl.query.view;

import farticle.domain.extension.query.ArticleViewFilter;
import farticle.domain.view.ArticleView;
import farticle.domain.view.MobileViewDecorator;
import fnote.domain.config.FlyContext;

public class MobileFitFilter implements ArticleViewFilter {
    @Override
    public void filter(ArticleView articleView, FlyContext flyContext) {
//        if (flyContext.isMobileSite()) {
//            articleView.setHtml(MobileViewDecorator.optimizView(articleView.getHtml()));
//        }
        articleView.setHtml(MobileViewDecorator.optimizView(articleView.getHtml()));

    }


}
