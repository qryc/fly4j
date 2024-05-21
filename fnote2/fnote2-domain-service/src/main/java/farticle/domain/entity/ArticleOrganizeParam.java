package farticle.domain.entity;

import fly4j.common.domain.IExtMap;

import java.util.HashMap;
import java.util.Map;

public class ArticleOrganizeParam {
    private ArticleOrganize articleOrganize = new ArticleOrganize();
    private int open;

    public ArticleOrganizeParam setArticleOrganize(ArticleOrganize articleOrganize) {
        this.articleOrganize = articleOrganize;
        return this;
    }

    public ArticleOrganize getArticleOrganize() {
        return articleOrganize;
    }

    public Integer getMaturity() {
        return articleOrganize.getMaturity();
    }

    public void setMaturity(Integer maturity) {
        articleOrganize.setMaturity(maturity);
    }

    public int getTextType() {
        return articleOrganize.getTextType();
    }

    public void setTextType(int textType) {
        articleOrganize.setTextType(textType);
    }

    public int getOrderNum() {
        return articleOrganize.getOrderNum();
    }

    public void setOrderNum(int orderNum) {
        this.articleOrganize.setOrderNum(orderNum);
    }

    public int getEditMode() {
        return articleOrganize.getEditMode();
    }

    public void setEditMode(int editMode) {
        this.articleOrganize.setEditMode(editMode);
    }

    public int getOpen() {
        return open;
    }

    public ArticleAuthEnum getAuthEnum() {
        return ArticleAuthEnum.getInsByAuthCode(this.getOpen());
    }


    public void setOpen(int open) {
        this.open = open;
    }

    public ArticleOrganizeParam setMdEditor(String mdEditor) {
        this.articleOrganize.setMdEditor(mdEditor);
        return this;
    }

    public String getMdEditor() {
        return articleOrganize.getMdEditor();
    }

    public String getWorkspace() {
        return articleOrganize.getWorkspace();
    }

    public void setWorkspace(String workspace) {
        this.articleOrganize.setWorkspace(workspace);
    }

    public boolean isInsurance() {
        return articleOrganize.isInsurance();
    }

    public boolean isTopping() {
        return articleOrganize.isTopping();
    }

    public void setTopping(boolean topping) {
        articleOrganize.setTopping(topping);
    }

    public ArticleOrganizeParam setInsurance(boolean insurance) {
        this.articleOrganize.setInsurance(insurance);
        return this;
    }

}
