package farticle.domain.extension.impl.query.single;

import farticle.domain.consts.FlyConst;
import farticle.domain.entity.ArticleOrganize;
import farticle.domain.entity.CplArticle;
import farticle.domain.extension.query.ArticleQueryParam;
import farticle.domain.extension.query.CplArticleFilter;
import fly4j.common.util.RepositoryException;
import org.apache.commons.lang3.StringUtils;

/**
 * 工作空间文章过滤
 */
public class WorkSpaceFilter implements CplArticleFilter {
    @Override
    public CplArticle filter(CplArticle cplArticle, ArticleQueryParam param) throws RepositoryException {
        var clientConfig = param.getFlyContext().clientConfig();
        String workspace = clientConfig.getWorkspace();
        return filter(cplArticle, workspace);
    }


    public static CplArticle filter(CplArticle cplArticle, String workspace) {
        if ("md".equals(cplArticle.getType())) {
            return cplArticle;
        }
        //没有设置默认按照严格的公司空间，工作空间过滤
        if (StringUtils.isBlank(workspace)) {
            workspace = ArticleOrganize.WORKSPACE_COMPANY;
        }

        if (ArticleOrganize.WORKSPACE_COMPANY.equals(workspace)) {
            for (String notName : FlyConst.workNotDisplayFileNames) {
                if (cplArticle.getNoteFileStr().contains(notName)) {
                    return null;
                }
            }

            //公司不展示个人
            if (ArticleOrganize.WORKSPACE_PERSON.equals(cplArticle.getArticleOrganize().getWorkspace())) {
                return null;
            } else {
                return cplArticle;
            }
        } else {
            //展示全部
            return cplArticle;
        }
    }

}