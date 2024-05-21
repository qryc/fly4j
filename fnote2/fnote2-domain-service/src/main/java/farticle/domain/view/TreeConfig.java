package farticle.domain.view;

import org.apache.commons.lang3.tuple.Pair;

import java.io.File;

public class TreeConfig {
    String target;
    String folderUrl;
    String fileUrl;
    boolean addOutLine;
    private boolean addParent;

    private boolean delEmpty;

    public String getTarget() {
        return target;
    }

    public TreeConfig setTarget(String target) {
        this.target = target;
        return this;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public TreeConfig setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
        return this;
    }

    public boolean isAddOutLine() {
        return addOutLine;
    }

    public TreeConfig setAddOutLine(boolean addOutLine) {
        this.addOutLine = addOutLine;
        return this;
    }

    public String getFolderUrl() {
        return folderUrl;
    }

    public TreeConfig setFolderUrl(String folderUrl) {
        this.folderUrl = folderUrl;
        return this;
    }

    public boolean isAddParent() {
        return addParent;
    }

    public void setAddParent(boolean addParent) {
        this.addParent = addParent;
    }

    public void setDelEmpty(boolean delEmpty) {
        this.delEmpty = delEmpty;
    }

    public boolean isDelEmpty() {
        return delEmpty;
    }

    public Pair<String, String> getIcon(File cfile) {
        if (cfile.getName().endsWith("flyNote")) {
            return Pair.of("/js/dtree/img/img1.png", "/js/dtree/img/img1.png");
        } else if (cfile.getName().endsWith(".md")) {
            return Pair.of("/js/dtree/img/md.png", "/js/dtree/img/md.png");
        } else if (cfile.getName().endsWith(".doc") || cfile.getName().endsWith(".docx")) {
            return Pair.of("/js/dtree/img/word.png", "/js/dtree/img/word.png");
        } else if (cfile.getName().endsWith(".ppt") || cfile.getName().endsWith(".pptx")) {
            return Pair.of("/js/dtree/img/ppt.png", "/js/dtree/img/ppt.png");
        } else {
            return null;
        }
    }
}

