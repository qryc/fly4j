package farticle.domain.view;

import farticle.domain.entity.ArticleContent;
import fly4j.common.util.DateUtil;
import fnote.domain.config.FlyContext;

/**
 * Created by qryc on 2021/11/10
 */
public class ArticleInfoVo {
    String noteFileStr;
    String titleHtml;
    String html;
    String createTimeStr;
    String modifyTimeStr;
    String articleViewMenu;

    public ArticleInfoVo(String noteFileStr, String titleHtml, String html, String createTimeStr, String modifyTimeStr, String articleViewMenu) {
        this.noteFileStr = noteFileStr;
        this.titleHtml = titleHtml;
        this.html = html;
        this.createTimeStr = createTimeStr;
        this.modifyTimeStr = modifyTimeStr;
        this.articleViewMenu = articleViewMenu;
    }

    public static ArticleInfoVo buildArticleInfoVo(ArticleView articleView, String articleViewMenu, FlyContext flyContext) {
        //标题不显示#
        ArticleContent content = articleView.getCplArticle().getArticleContent();
        content.setTitle(content.title().replaceAll("#", "").trim());
        //添加锚点
        return new ArticleInfoVo(articleView.getCplArticle().getNoteFileStr()
                , articleView.getTitleHtml()
                , articleView.getHtml()
                , DateUtil.getDateStr(articleView.getCplArticle().getCreateTime())
                , DateUtil.getDateStr(articleView.getCplArticle().getModifyTime())
                , articleViewMenu);
    }

    public String getNoteFileStr() {
        return noteFileStr;
    }

    public void setNoteFileStr(String noteFileStr) {
        this.noteFileStr = noteFileStr;
    }

    public String getTitleHtml() {
        return titleHtml;
    }

    public void setTitleHtml(String titleHtml) {
        this.titleHtml = titleHtml;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public String getCreateTimeStr() {
        return createTimeStr;
    }

    public void setCreateTimeStr(String createTimeStr) {
        this.createTimeStr = createTimeStr;
    }

    public String getModifyTimeStr() {
        return modifyTimeStr;
    }

    public void setModifyTimeStr(String modifyTimeStr) {
        this.modifyTimeStr = modifyTimeStr;
    }

    public String getArticleViewMenu() {
        return articleViewMenu;
    }

    public void setArticleViewMenu(String articleViewMenu) {
        this.articleViewMenu = articleViewMenu;
    }
}
