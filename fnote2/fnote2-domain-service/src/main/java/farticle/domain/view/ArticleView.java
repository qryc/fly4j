package farticle.domain.view;

import farticle.domain.entity.CplArticle;

public class ArticleView {
    private final CplArticle cplArticle;
    private String html;

    public ArticleView(CplArticle cplArticle) {
        this.cplArticle = cplArticle;
        this.html = cplArticle.getArticleContent().content();
    }



    public String getOriginalText() {
        return cplArticle.getArticleContent().content();
    }


    public String getHtml() {
        return html;
    }


    public Mark getMark() {
        Mark mark = new Mark();
        mark.appendMark(getLengthMark());
        if (cplArticle.getArticleOrganize().getMaturity() != null && cplArticle.getArticleOrganize().getMaturity() >= 999) {
            mark.appendMark("发表");
        }
        return mark;

    }

    public String getLengthMark() {
        if (null == getOriginalText() || getOriginalText().length() < 50) {
            return ("50");
        } else if (getOriginalText().length() < 100) {
            return ("100");
        } else if (getOriginalText().length() < 200) {
            return ("200");
        } else {
            return "";
        }
    }

    public String getTitleHtml() {
        StringBuilder titleHtml = new StringBuilder();
        var title = this.getCplArticle().getArticleContent().title();
        switch (this.getCplArticle().getArticleContent().authEnum()) {
            case PRI, ENCRYPT_PUB, ENCRYPT_PRI -> titleHtml.append("<font color='red'>").append(title).append(" </font>");
            case OPEN, REAL_OPEN -> titleHtml.append(title);
        }
        ArticleView4List.buildMark(cplArticle).getMarks().forEach(str -> {
            titleHtml.append(" <span style='font-size: 50%%' class='badge badge-secondary'>%s</span>".formatted(str));
        });
        return titleHtml.toString();
    }


    public CplArticle getCplArticle() {
        return cplArticle;
    }

    public void setHtml(String html) {
        this.html = html;
    }
}
