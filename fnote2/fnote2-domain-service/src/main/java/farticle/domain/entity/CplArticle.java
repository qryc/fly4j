package farticle.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fly4j.common.crypto.AESUtil;
import fly4j.common.util.FlyPreconditions;
import fly4j.common.util.FlyString;
import fnote.user.domain.entity.BaseDomain;
import fnote.user.domain.entity.IdPin;
import org.apache.commons.lang3.StringUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class CplArticle extends BaseDomain<CplArticle> implements Cloneable {
    private ArticleContent articleContent;
    private ArticleOrganize articleOrganize;
    //flyNote,md
    private String type = "flyNote";
    //git 使用
    protected Long extId;


    @Override
    public CplArticle clone() throws CloneNotSupportedException {
        CplArticle cplArticle = (CplArticle) super.clone();
        cplArticle.articleContent = (ArticleContent) articleContent.clone();
        cplArticle.articleOrganize = (ArticleOrganize) articleOrganize.clone();
        return cplArticle;
    }

    @Override
    public boolean equals(Object o) {
        CplArticle that = (CplArticle) o;
        return getNoteFileStr().equals(that.getNoteFileStr());
    }

    @Override
    public int hashCode() {
        return Objects.hash(articleContent, articleOrganize);
    }

    @Override
    public String toString() {
        return "CplArticle{" +
                "articleContent=" + articleContent +
                ", articleOrganize=" + articleOrganize +
                '}';
    }

    public CplArticle() {
    }

    public CplArticle(ArticleContent articleContent, ArticleOrganize articleOrganize) {
        this.articleContent = articleContent;
        this.articleOrganize = articleOrganize;
        if (null == this.getArticleOrganize()) {
            this.setArticleOrganize(new ArticleOrganize());
        }
    }


    public static CplArticle newStoreInstance(ArticleContent articleContent, ArticleOrganize articleOrganize) {
        var cplArticle = new CplArticle(articleContent, articleOrganize);
        return cplArticle;
    }


    public CplArticle decryptArticle(String encryptPwd) {
        if (ArticleAuthEnum.REAL_OPEN.equals(articleContent.authEnum())) {
            return this;
        }
        if (ArticleAuthEnum.ENCRYPT_PRI.equals(articleContent.authEnum())) {
            //            throw new RuntimeException("ENCRYPT_PRI don't have cryptPass");
            return this;
        }
        if (StringUtils.isEmpty(encryptPwd)) {
            throw new RuntimeException("articlePwd is Empty");
        }
        if (StringUtils.isNotBlank(articleContent.content())) {
            articleContent = articleContent.of(AESUtil.decryptHexStrToStr(articleContent.content, encryptPwd));
        } else {
            throw new RuntimeException("content is Empty");
        }
        return this;
    }

    public CplArticle encryptArticle(String encryptPwd) {
        articleContent = articleContent.encryptArticle(encryptPwd);
        return this;
    }


    public void updateArticleContent(ArticleContent articleContent) {
        this.setModifyTime(new Date());
        this.setArticleContent(articleContent);
    }


    public void appendFrontOfContent(final IdPin idPin, String appendStr) {
        FlyPreconditions.requireNotEmpty(idPin.getNoteFileStr());
        FlyPreconditions.requireNotEmpty(appendStr);

        var articleContent = Objects.requireNonNull(this.getArticleContent());
        var organize = Objects.requireNonNull(this.getArticleOrganize());

        var content = "";
        if (StringUtils.isBlank(articleContent.content())) {
            switch (organize.getTextType()) {
                case ArticleOrganize.TYPE_MD -> content = appendStr;
                case ArticleOrganize.TYPE_HTML -> content = appendStr.replaceAll(StringUtils.LF, "<br/>");
            }
        } else {
            switch (organize.getTextType()) {
                case ArticleOrganize.TYPE_MD -> content = appendStr + StringUtils.LF + articleContent.content();
                case ArticleOrganize.TYPE_HTML ->
                        content = appendStr.replaceAll(StringUtils.LF, "<br/>") + "<br/>" + articleContent.content();
            }
        }
        this.setArticleContent(new ArticleContent(articleContent.authEnum(), articleContent.title(), content));

        this.setModifyTime(new Date());

        if (null == this.getArticleOrganize()) {
            this.articleOrganize = new ArticleOrganize();
        } else {
            //已经有组织关系，不再进行设置
        }
    }

    public void organizeArticle(final ArticleOrganize organizeParam, final IdPin idPin) {
        FlyPreconditions.requireNotEmpty(idPin.getNoteFileStr(), "update article id can't be null");
        this.setModifyTime(new Date());
        this.articleOrganize = organizeParam;
    }

    public Date getModifyTime4Sort() {
        Date date = this.getModifyTime() != null ? this.getModifyTime() : this.getCreateTime();
        if ("other".equals(this.getArticleOrganize().getFileType())) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DATE, -100);
            return calendar.getTime();
        } else {
            return date;
        }

    }

    public void encodeHtml() {
        this.setArticleContent(this.articleContent.of(FlyString.htmEncode(this.getArticleContent().content())));
    }

    @JsonIgnore
    public IdPin getIdPin() {
        return IdPin.of(this.getPin(), this.noteFileStr);
    }

    public boolean canDel() {
//        return (this.getArticleContent().title().contains("删除") || this.getArticleContent().title().contains("delete"));
        return true;
    }


    public ArticleContent getArticleContent() {
        return articleContent;
    }

    public String getArticleContentStr() {
        return articleContent.content;
    }

    public void setArticleContent(ArticleContent articleContent) {
        this.articleContent = articleContent;
    }

    public void setArticleContentStr(String content) {
        this.articleContent.content = content;
    }

    public void alterAuthEnum(ArticleAuthEnum authEnum) {
        this.articleContent = new ArticleContent(authEnum, this.articleContent.title, this.articleContent.content);
    }

    public ArticleOrganize getArticleOrganize() {
        return articleOrganize;
    }

    public void setArticleOrganize(ArticleOrganize articleOrganize) {
        this.articleOrganize = articleOrganize;
    }


    //去除ID，作为扩展属性
    public Long getExtId() {
        return extId;
    }

    public CplArticle setExtId(Long extId) {
        this.extId = extId;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
