package farticle.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 分类和标签。
 * 主标签就是分类
 * 标签独立维护。subject
 */
public class ArticleOrganize implements Cloneable {
    public static final int TYPE_MD = 1;
    public static final int TYPE_HTML = 0;
    /// 博客组织-编辑模式
    public static final int EDITMODE_EDIT = 0;
    public static final int EDITMODE_APPEND = 1;
    public static final int EDITMODE_READ = 2; //可读，可删除
    public static final int EDITMODE_LOCK = 3; //可读，不可删除
    /// 博客组织-extMap的Key
    public static final String ORGANIZE_TOPPING = "topping";
    public static final String ORGANIZE_INSURANCE = "insurance";
    public static final String ORGANIZE_MDEDITOR = "mdEditor";
    public static final String ORGANIZE_DEFAULT = "0";//文本编辑器
    public static final String ORGANIZE_MDEDITOR_TEXT = "1";//文本编辑器
    public static final String ORGANIZE_MDEDITOR_CK = "2";//ckEditor
    /// 博客组织-工作空间
    public static final String WORKSPACE_COMPANY = "company";
    public static final String WORKSPACE_PERSON = "person";
    // 成熟度
    private Integer maturity;
    private String fileType;
    // 文件的文本格式：html,markdown
    private int textType;
    private int orderNum = 100;
    //0普通编辑 1 追加
    private int editMode;
    private String workspace;
    private boolean topping;
    private boolean insurance;
    //编辑器：文本，MD可视，tinymce富文本，ckeditor富文本
    private String mdEditor;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @JsonIgnore
    public boolean isTextType(int txtType) {
        return txtType == this.textType;
    }

    @JsonIgnore
    public boolean isHtmlType() {
        return ArticleOrganize.TYPE_HTML == this.textType;
    }

    @JsonIgnore
    public boolean isMdType() {
        return ArticleOrganize.TYPE_MD == this.textType;
    }


    public Integer getMaturity() {
        return maturity;
    }

    public ArticleOrganize setMaturity(Integer maturity) {
        this.maturity = maturity;
        return this;
    }


    public int getTextType() {
        return textType;
    }

    public ArticleOrganize setTextType(int textType) {
        this.textType = textType;
        return this;
    }

    public int getOrderNum() {
        return orderNum;
    }

    public ArticleOrganize setOrderNum(int orderNum) {
        this.orderNum = orderNum;
        return this;
    }

    public int getEditMode() {
        return editMode;
    }

    public ArticleOrganize setEditMode(int editMode) {
        this.editMode = editMode;
        return this;
    }


    public String getWorkspace() {
        return workspace;
    }

    public void setWorkspace(String workspace) {
        this.workspace = workspace;
    }

    public boolean isTopping() {
        return topping;
    }

    public void setTopping(boolean topping) {
        this.topping = topping;
    }

    public boolean isInsurance() {
        return insurance;
    }

    public void setInsurance(boolean insurance) {
        this.insurance = insurance;
    }

    public String getMdEditor() {
        return mdEditor;
    }

    public void setMdEditor(String mdEditor) {
        this.mdEditor = mdEditor;
    }

    public String getFileType() {
        return fileType;
    }

    public ArticleOrganize setFileType(String fileType) {
        this.fileType = fileType;
        return this;
    }
}
