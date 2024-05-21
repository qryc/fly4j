package fnote.web.controller;

import fnote.domain.config.FlyContext;
import farticle.domain.entity.CplArticle;
import fnote.article.web.controller.NoteFileStrUtil;

/**
 * Created by qryc on 2021/11/8
 */
public class ArticleMenuService {
    public static String getArticleViewMenu(FlyContext flyContext, CplArticle cplArticle) {
        //全屏或者手机不展示
        if (!flyContext.isLogin() || flyContext.isMobileSite()) {
            return "";
        }
        StringBuilder menuHtml = new StringBuilder();
        String noteFileStrEncode= NoteFileStrUtil.encode(cplArticle.getNoteFileStr());
        if (cplArticle.getArticleOrganize().isHtmlType()) {
            if (cplArticle.getArticleOrganize().getEditMode() == 0) {//普通编辑模式
                menuHtml.append("<a href='/articleMaintain/toEditArticle.do?edithome=list&noteFileStr=%s'>编辑</a> ".formatted(noteFileStrEncode));
            }
        }
        if (cplArticle.getArticleOrganize().isMdType()) {
            if (cplArticle.getArticleOrganize().getEditMode() == 0) {//普通编辑模式
                menuHtml.append(" <a href='/articleMaintain/toEditArticle.do?noteFileStr=%s'>简辑</a> ".formatted(noteFileStrEncode));
            }
        }
        menuHtml.append(" <a href='/articleMaintain/toOrganizeBlog.do?edithome=list&noteFileStr=%s'>组织</a> ".formatted(noteFileStrEncode));
        if (cplArticle.canDel()) {
            menuHtml.append("<a href=\"#\" onclick=\"window.confirm('确定删除?!')?this.href='/articleMaintain/del.do?noteFileStr=%s':this.href='javascript:void()';\">删除</a> ".formatted(noteFileStrEncode));
        }
        menuHtml.append(" <a href='/article/articles.do?buId=lastEdit'>近期</a> ".formatted(noteFileStrEncode));

        return menuHtml.toString();
    }

}
