package farticle.domain.entity;

/**
 * 类之间谁依赖谁不固定，干脆独立出来负责转换
 */
public class BeanConvert {
    public static ArticleOrganizeParam organize2Param(CplArticle cplArticle) {
        ArticleOrganizeParam organizeParam = new ArticleOrganizeParam();
        organizeParam.setArticleOrganize(cplArticle.getArticleOrganize());
        organizeParam.setOpen(cplArticle.getArticleContent().authEnum().getAuthCode());
        return organizeParam;
    }

    public static ArticleOrganize organizeParam2Orgnize(ArticleOrganizeParam param) {
        return param.getArticleOrganize();
    }
}
