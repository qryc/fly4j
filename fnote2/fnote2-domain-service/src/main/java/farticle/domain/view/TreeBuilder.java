package farticle.domain.view;

import farticle.domain.entity.DtreeObj;

import java.io.File;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class TreeBuilder {
    public TreeBuilder buildUrlTarget(DtreeObj dtreeObj, TreeConfig treeConfig, File cfile) {
        dtreeObj.setUrl(treeConfig.getFileUrl() + URLEncoder.encode(cfile.getAbsolutePath(), StandardCharsets.UTF_8));
        dtreeObj.setTarget(treeConfig.getTarget());

        return this;
    }

    public DtreeObj buildIcon(DtreeObj dtreeObj, File cfile) {

        setIcon4File(cfile, dtreeObj);
        return dtreeObj;
    }

    public static void setIcon4File(File cfile, DtreeObj dtreeObj) {
        if (cfile.getName().endsWith("flyNote")) {
            dtreeObj.setIcon("/js/dtree/img/img1.png");
            dtreeObj.setIconOpen("/js/dtree/img/img1.png");
        } else if (cfile.getName().endsWith(".md")) {
            dtreeObj.setIcon("/js/dtree/img/md.png");
            dtreeObj.setIconOpen("/js/dtree/img/md.png");
        } else if (cfile.getName().endsWith(".doc") || cfile.getName().endsWith(".docx")) {
            dtreeObj.setIcon("/js/dtree/img/word.png");
            dtreeObj.setIconOpen("/js/dtree/img/word.png");
        } else if (cfile.getName().endsWith(".ppt") || cfile.getName().endsWith(".pptx")) {
            dtreeObj.setIcon("/js/dtree/img/ppt.png");
            dtreeObj.setIconOpen("/js/dtree/img/ppt.png");
        }
    }
}
