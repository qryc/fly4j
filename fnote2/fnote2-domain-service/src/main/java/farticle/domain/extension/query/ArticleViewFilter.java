
package farticle.domain.extension.query;

import farticle.domain.view.ArticleView;
import fnote.domain.config.FlyContext;

/**
 * Created by qryc on 2019/12/1.
 */
public interface ArticleViewFilter {
    public void filter(ArticleView articleView, FlyContext flyContext);
}
