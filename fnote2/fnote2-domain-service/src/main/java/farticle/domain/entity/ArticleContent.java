package farticle.domain.entity;

import fly4j.common.crypto.AESUtil;
import fly4j.common.util.StringConst;
import org.apache.commons.lang3.StringUtils;

public class ArticleContent implements Cloneable {
    ArticleAuthEnum authEnum;
    String title;
    String content;

    public ArticleContent(ArticleAuthEnum authEnum, String title, String content) {
        this.authEnum = authEnum;
        this.title = title;
        this.content = content;
    }

    public static ArticleContent buildArticleContent(ArticleAuthEnum authEnum, String titleAndContent, String split) {
        var indexTitle = titleAndContent.indexOf(split);
        if (indexTitle == -1) {
            //如果没有换行符，使用全部做标题
            return new ArticleContent(authEnum, titleAndContent, "");
        } else {
            //根据内容截断第一行为标题,剩下的为内容
            var title = titleAndContent.substring(0, indexTitle).trim();
            //截断内容
            var content = titleAndContent.substring(indexTitle + 1);
            //截断空行
            if (content.endsWith(StringConst.LF)) {
                content = content.substring(0, content.length() - 1);
            }
            return new ArticleContent(authEnum, title, content);

        }
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public ArticleContent of(String content) {
        return new ArticleContent(this.authEnum, this.title, content);
    }

    public ArticleContent of(ContentParam contentParam) {
        return new ArticleContent(this.authEnum, contentParam.title(), contentParam.content());
    }

    public static ArticleContent of(ArticleAuthEnum authEnum, ContentParam contentParam) {
        return new ArticleContent(authEnum, contentParam.title(), contentParam.content());
    }

    public ArticleContent of(String title, String content) {
        return new ArticleContent(this.authEnum, title, content);
    }

    public ArticleContent encryptArticle(String articlePwd) {
        if (ArticleAuthEnum.REAL_OPEN.equals(this.authEnum())) {
            return this;
        }
        if (ArticleAuthEnum.ENCRYPT_PRI.equals(this.authEnum)) {
            throw new RuntimeException("ENCRYPT_PRI don't have cryptPass");
        }
        if (StringUtils.isEmpty(articlePwd)) {
            throw new RuntimeException("articlePwd is Empty");
        }
        if (StringUtils.isNotBlank(this.content())) {
            return new ArticleContent(authEnum, title, AESUtil.encryptStr2HexStr(this.content(), articlePwd));
        } else {
            throw new RuntimeException("content is Empty");
        }
    }

    public boolean isInsurance() {
        return this.title().startsWith("*");
    }

    public ArticleAuthEnum authEnum() {
        return authEnum;
    }

    public String title() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String content() {
        return content;
    }

    public void replaceContentGtLt() {
        this.content = this.content.replaceAll("&lt;", "<").replaceAll("&gt;", ">")
                .replaceAll("&lsquo;", "\"");
    }
}
