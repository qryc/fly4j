package fnote.domain.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ViewConfig {
    private Double fontEm = 1.5d;
    private String viewArticleCss = "viewArticleDefault.css";

    public Double getFontEm() {
        return fontEm;
    }

    public void setFontEm(Double fontEm) {
        this.fontEm = fontEm;
    }

    public String getViewArticleCss() {
        return viewArticleCss;
    }

    public void setViewArticleCss(String viewArticleCss) {
        this.viewArticleCss = viewArticleCss;
    }
}
