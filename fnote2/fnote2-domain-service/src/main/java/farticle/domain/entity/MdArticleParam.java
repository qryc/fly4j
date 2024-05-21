package farticle.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * @author qryc
 */
public class MdArticleParam implements Serializable {
    private static final long serialVersionUID = -6970967506712260305L;
    private Long extId;
    private String noteFileStr;
    private String pin;
    private String mdContent;
    private String workspace;


    @JsonIgnore
    public static MdArticleParam convert2MdParam(CplArticle cplArticle) {
        var mdStrBuilder = new StringBuilder(cplArticle.getArticleContent().title()).append(StringUtils.LF);
        if (StringUtils.isNotBlank(cplArticle.getArticleContent().content()))
            mdStrBuilder.append(cplArticle.getArticleContent().content());
        MdArticleParam mdArticleParam = new MdArticleParam()
                .setMdContent(mdStrBuilder.toString());

        if (StringUtils.isNotBlank(cplArticle.getNoteFileStr())) {
            mdArticleParam.setNoteFileStr(URLEncoder.encode(cplArticle.getNoteFileStr(), StandardCharsets.UTF_8));
            mdArticleParam.extId = cplArticle.getExtId();
        }
        return mdArticleParam;

    }

    public ContentParam convert2ArticleContent() {
        var title = "";
        var content = "";
        mdContent = mdContent.replaceAll(StringUtils.CR + StringUtils.LF, StringUtils.LF);
        var indexTitle = mdContent.indexOf(StringUtils.LF);
        if (indexTitle == -1) {
            //如果没有换行符，使用全部做标题
            title = mdContent;
        } else {
            //根据内容截断第一行为标题,剩下的为内容
            title = mdContent.substring(0, indexTitle).trim();
            content = mdContent.substring(indexTitle + 1);
        }

        /**
         * 处理加密
         * 以**结尾的，为需要加密
         * 以*结尾的，为隐私
         */
        return new ContentParam(title, content);
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }


    public String getMdContent() {
        return mdContent;
    }

    public MdArticleParam setMdContent(String mdContent) {
        this.mdContent = mdContent;
        return this;
    }

    public String getPin() {
        return pin;
    }

    public MdArticleParam setPin(String pin) {
        this.pin = pin;
        return this;
    }

    public String getWorkspace() {
        return workspace;
    }

    public void setWorkspace(String workspace) {
        this.workspace = workspace;
    }

    public MdArticleParam setNoteFileStr(String noteFileStr) {
        this.noteFileStr = noteFileStr;
        return this;
    }

    public String getNoteFileStr() {
        return noteFileStr;
    }

    public void setExtId(Long extId) {
        this.extId = extId;
    }

    public Long getExtId() {
        return extId;
    }
}
