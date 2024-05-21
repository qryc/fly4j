package farticle.domain.extension.impl.query.view;

import farticle.domain.consts.FlyConst;
import farticle.domain.entity.ArticleOrganize;
import farticle.domain.extension.query.ArticleViewFilter;
import farticle.domain.view.ArticleView;
import fnote.domain.config.FlyConfig;
import fnote.domain.config.FlyContext;

public class WorkSpaceViewFilter implements ArticleViewFilter {
    @Override
    public void filter(ArticleView articleView, FlyContext flyContext) {
        if (ArticleOrganize.WORKSPACE_COMPANY.equals(flyContext.clientConfig().getWorkspace())) {
            for (String notName : FlyConst.workNotDisplayFileNames) {
                if (articleView.getCplArticle().getNoteFileStr().contains(notName)) {
                    throw new RuntimeException("工作空间没有授权");
                }
            }
            if (articleView.getCplArticle().getNoteFileStr().contains("9999")) {
                throw new RuntimeException("工作空间没有授权");
            }

        }
    }
}
