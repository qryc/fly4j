package fnote.domain.config;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fly4j.common.domain.IExtMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by qryc on 2019/6/8.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClientConfig {

    /**
     * 可以查看的文章类型
     * simple normal
     */
    private String vm = "normal";

    private Double fontEm = 1.5d;
    private String ckWidth;
    private String mainDivMargin;
    private boolean insurance;
    private boolean showFile;
    private boolean fullScreen;
    private boolean articleFolderSelect;
    private boolean pageTree;
    private boolean child2parent;
    private boolean showHis;
    private String workPath;
    private String workspace;
    private String mdEditor;
    private boolean showEditorMenu;
    private boolean ajaxSave;
    private List<String> showFolderList = new ArrayList<>();

    @Override
    public String toString() {
        return "ClientConfig{" +
                "vm='" + vm + '\'' +
                ", fontEm=" + fontEm +
                ", ckWidth='" + ckWidth + '\'' +
                ", mainDivMargin='" + mainDivMargin + '\'' +
                ", insurance=" + insurance +
                ", showFile=" + showFile +
                ", fullScreen=" + fullScreen +
                ", articleFolderSelect=" + articleFolderSelect +
                ", pageTree=" + pageTree +
                ", child2parent=" + child2parent +
                ", showHis=" + showHis +
                ", workPath='" + workPath + '\'' +
                ", workspace='" + workspace + '\'' +
                ", mdEditor=" + mdEditor +
                ", showEditorMenu=" + showEditorMenu +
                ", ajaxSave=" + ajaxSave +
                ", showFolderList=" + showFolderList +
                '}';
    }

    @JsonIgnore
    public boolean isNormalScreen() {
        return !this.fullScreen;
    }

    @JsonIgnore
    public boolean isFullScreenReverse() {
        return !this.fullScreen;
    }


    public String getVm() {
        return vm;
    }

    public ClientConfig setVm(String vm) {
        this.vm = vm;
        return this;
    }


    public Double getFontEm() {
        return fontEm;
    }

    public ClientConfig setFontEm(Double fontEm) {
        this.fontEm = fontEm;
        return this;
    }


    public void setShowFolderList(List<String> showFolderList) {
        this.showFolderList = showFolderList;
    }

    public List<String> getShowFolderList() {
        return showFolderList;
    }

    public String getCkWidth() {
        return ckWidth;
    }

    public void setCkWidth(String ckWidth) {
        this.ckWidth = ckWidth;
    }

    public String getMainDivMargin() {
        return mainDivMargin;
    }

    public void setMainDivMargin(String mainDivMargin) {
        this.mainDivMargin = mainDivMargin;
    }

    public boolean isInsurance() {
        return insurance;
    }

    public void setInsurance(boolean insurance) {
        this.insurance = insurance;
    }

    public boolean isShowFile() {
        return showFile;
    }

    public void setShowFile(boolean showFile) {
        this.showFile = showFile;
    }

    public boolean isFullScreen() {
        return fullScreen;
    }

    public void setFullScreen(boolean fullScreen) {
        this.fullScreen = fullScreen;
    }

    public boolean isArticleFolderSelect() {
        return articleFolderSelect;
    }

    public void setArticleFolderSelect(boolean articleFolderSelect) {
        this.articleFolderSelect = articleFolderSelect;
    }

    public boolean isPageTree() {
        return pageTree;
    }

    public void setPageTree(boolean pageTree) {
        this.pageTree = pageTree;
    }

    public boolean isChild2parent() {
        return child2parent;
    }

    public void setChild2parent(boolean child2parent) {
        this.child2parent = child2parent;
    }

    public boolean isShowHis() {
        return showHis;
    }

    public void setShowHis(boolean showHis) {
        this.showHis = showHis;
    }

    public String getWorkPath() {
        return workPath;
    }

    public void setWorkPath(String workPath) {
        this.workPath = workPath;
    }

    public String getWorkspace() {
        return workspace;
    }

    public void setWorkspace(String workspace) {
        this.workspace = workspace;
    }

    public void setMdEditor(String mdEditor) {
        this.mdEditor = mdEditor;
    }

    public String getMdEditor() {
        return mdEditor;
    }

    public boolean isShowEditorMenu() {
        return showEditorMenu;
    }

    public void setShowEditorMenu(boolean showEditorMenu) {
        this.showEditorMenu = showEditorMenu;
    }

    public boolean isAjaxSave() {
        return ajaxSave;
    }

    public void setAjaxSave(boolean ajaxSave) {
        this.ajaxSave = ajaxSave;
    }
}
