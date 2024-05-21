package farticle.domain.view;

import farticle.domain.entity.Dtree;
import farticle.domain.entity.DtreeObj;
import fnote.domain.config.FlyContext;

import java.io.File;

public class ArticleTreeSpiParam {
    private FlyContext flyContext;
    private Dtree dtree;
    private DtreeObj dtreeObj;
    private File file;
    private String sceneName;
    private TreeConfig treeConfig;


    public ArticleTreeSpiParam(FlyContext flyContext, Dtree dtree, File file) {
        this.flyContext = flyContext;
        this.dtree = dtree;
        this.file = file;
    }

    public FlyContext getFlyContext() {
        return flyContext;
    }

    public Dtree getDtree() {
        return dtree;
    }

    public File getFile() {
        return file;
    }

    public ArticleTreeSpiParam setSceneName(String sceneName) {
        this.sceneName = sceneName;
        return this;
    }

    public String getSceneName() {
        return sceneName;
    }

    public DtreeObj getDtreeObj() {
        return dtreeObj;
    }

    public void setDtreeObj(DtreeObj dtreeObj) {
        this.dtreeObj = dtreeObj;
    }

    public TreeConfig getTreeConfig() {
        return treeConfig;
    }

    public void setTreeConfig(TreeConfig treeConfig) {
        this.treeConfig = treeConfig;
    }
}
