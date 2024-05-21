package farticle.domain.entity;

/**
 *  @author qryc
 */
public class ArticleParam{
    private ArticleContent articleContent;
    private Long id;
    private String pin;

    public ArticleContent getArticleContent() {
        return articleContent;
    }

    public ArticleParam setArticleContent(ArticleContent articleContent) {
        this.articleContent = articleContent;
        return this;
    }

    public Long getId() {
        return id;
    }

    public ArticleParam setId(Long id) {
        this.id = id;
        return this;
    }

    public String getPin() {
        return pin;
    }

    public ArticleParam setPin(String pin) {
        this.pin = pin;
        return this;
    }
}
