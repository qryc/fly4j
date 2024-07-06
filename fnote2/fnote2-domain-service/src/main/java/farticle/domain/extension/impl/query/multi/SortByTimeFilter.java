package farticle.domain.extension.impl.query.multi;

import farticle.domain.entity.CplArticle;
import farticle.domain.extension.query.ArticleQueryParam;
import farticle.domain.extension.query.CplArticlesFilter;

import java.util.stream.Stream;

/**
 * Created by qryc on 2019/12/1.
 */
public class SortByTimeFilter implements CplArticlesFilter {
    @Override
    public Stream<CplArticle> filter(Stream<CplArticle> cplArticles, ArticleQueryParam param) {
        return cplArticles.sorted((o1, o2) -> {
            //如果两个对象只有一个置顶
            boolean topping1 = o1.getArticleOrganize().isTopping();
            boolean topping2 = o2.getArticleOrganize().isTopping();
            if (topping1 && !topping2) {
                return -1;
            } else if (!topping1 && topping2) {
                return 1;
            }
            return o2.getModifyTime4Sort().compareTo(o1.getModifyTime4Sort());
        });
    }

}
