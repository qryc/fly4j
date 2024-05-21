package farticle.domain.view;

import farticle.domain.entity.ArticleOrganize;
import farticle.domain.entity.CplArticle;
import farticle.domain.entity.Dtree;
import farticle.domain.entity.DtreeObj;
import flynote.article.query.ArticleLoad;
import fnote.domain.config.FlyContext;
import org.apache.commons.lang3.StringUtils;

import java.io.File;

public class ArticleOutlineTree implements ArticleTreeSPI {
    private ArticleLoad articleLoad;

    @Override
    public void buildTree(ArticleTreeSpiParam param, TreeService treeService) {
        /**
         * 顶层树添加outLine
         */
        FlyContext flyContext = param.getFlyContext();
        Dtree dtree = param.getDtree();
        File file = param.getFile();
        DtreeObj dtreeObjRoot = dtree.getRoot();
        CplArticle cplArticle = articleLoad.getCplArticle(flyContext, file.getAbsolutePath());
        if (cplArticle == null) {
            return;
        }
        //设置文章的图标
        ArticleOutlineChildTree.setIcon4CplArticle(cplArticle, dtreeObjRoot);
        //构建文章内容的导航树
        ArticleOutlineTree.parseMdStr2OutLine(dtree, cplArticle.getArticleContent().content(), dtreeObjRoot);


    }
    public static void parseMdStr2OutLine(Dtree dtree, String content, DtreeObj dtreeObjParent) {
        var mdStr = content;
        var id = 0;//序号减去1，是因为后用
        for (var lineStr : mdStr.split(StringUtils.LF)) {
            boolean add = false;
            if (lineStr.startsWith("######")) {

            } else if (lineStr.startsWith("#####")) {

            } else if (lineStr.startsWith("####")) {

            } else if (lineStr.startsWith("###")) {
                add = true;
                id++;
            } else if (lineStr.startsWith("##")) {
                add = true;
                id++;
            } else if (lineStr.startsWith("#")) {
                add = true;
                id++;
            }
            if (add) {
                String name = lineStr.replaceAll("#", "").trim();
                DtreeObj dtreeObj = dtree.addDtreeObjNotTrim(dtreeObjParent.getId(), name, "");
                dtreeObj.setIcon("/js/dtree/img/empty.gif");
                dtreeObj.setIconOpen("/js/dtree/img/empty.gif");
                dtreeObj.setUrl("#section-" + (id));
            }
        }
    }

    public void setArticleLoad(ArticleLoad articleLoad) {
        this.articleLoad = articleLoad;
    }

}
