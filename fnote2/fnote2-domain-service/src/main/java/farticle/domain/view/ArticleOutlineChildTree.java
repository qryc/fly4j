package farticle.domain.view;

import farticle.domain.entity.ArticleOrganize;
import farticle.domain.entity.CplArticle;
import farticle.domain.entity.Dtree;
import farticle.domain.entity.DtreeObj;
import flynote.article.query.ArticleLoad;
import fnote.domain.config.FlyContext;

import java.io.File;

public class ArticleOutlineChildTree implements ArticleTreeSPI {
    private ArticleLoad articleLoad;

    @Override
    public void buildTree(ArticleTreeSpiParam param, TreeService treeService) {
        /**
         * 顶层树添加outLine
         */
        FlyContext flyContext = param.getFlyContext();
        Dtree dtree = param.getDtree();
        File file = param.getFile();
        dealWithFile(dtree, param.getTreeConfig(), flyContext, param.getDtreeObj(), file);

    }

    public void dealWithFile(Dtree dtree, TreeConfig treeConfig, FlyContext flyContext, DtreeObj dtreeObj, File cfile) {
        if (cfile.getName().endsWith("flyNote")) {
            //查询文章信息
            CplArticle cplArticle = articleLoad.getCplArticle(flyContext, cfile.getAbsolutePath());
            if (cplArticle == null) {
                return;
            }
            //设置图标
            setIcon4CplArticle(cplArticle, dtreeObj);
            //构建导航树
            if (treeConfig.isAddOutLine()) {
                ArticleOutlineTree.parseMdStr2OutLine(dtree, cplArticle.getArticleContent().content(), dtreeObj);
            }
        }
    }


    public static void setIcon4CplArticle(CplArticle cplArticle, DtreeObj dtreeObjParent) {

        //根据文章类型设置图标
        if (ArticleOrganize.TYPE_HTML == cplArticle.getArticleOrganize().getTextType()) {
            dtreeObjParent.setIcon("/js/dtree/img/tiny.png");
            dtreeObjParent.setIconOpen("/js/dtree/img/tiny.png");
        } else {
            if (ArticleOrganize.ORGANIZE_MDEDITOR_CK.equals(cplArticle.getArticleOrganize().getMdEditor())) {
                dtreeObjParent.setIcon("/js/dtree/img/ck.png");
                dtreeObjParent.setIconOpen("/js/dtree/img/ck.png");
            } else {
                dtreeObjParent.setIcon("/js/dtree/img/md.png");
                dtreeObjParent.setIconOpen("/js/dtree/img/md.png");
            }
        }
    }

    public void setArticleLoad(ArticleLoad articleLoad) {
        this.articleLoad = articleLoad;
    }

}
