package farticle.domain.view;

import farticle.domain.entity.Dtree;
import farticle.domain.entity.DtreeObj;
import flynote.article.query.ArticleLoad;
import fnote.common.PathService;
import fnote.domain.config.FlyContext;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class TreeService {


    static final Logger log = LoggerFactory.getLogger(TreeService.class);
    private ArticleLoad articleLoad;
    private PathService pathService;
    static String articlesUrl = "/article/articles.do?buId=lastEdit&rootPath=";
    public static String viewUrl = "/article/viewArticle.do?noteFileStr=";
    public static String editUrl = "/articleMaintain/toEditArticle.do?edithome=list&noteFileStr=";
    public static String browserUrl = "/browser/list.do?sort=1&currDir=";
    protected Map<String, TreeConfig> treeConfigMap;

    private Map<String, List<ArticleTreeSPI>> articleTreeMap;
    private Map<String, FileFilter> predicateMap;


    public void setPredicateMap(Map<String, FileFilter> predicateMap) {
        this.predicateMap = predicateMap;
    }

    /**
     * 把指定的文件以及子文件加入树
     *
     * @param file         文件夹地址
     * @param parentTreeTd 父亲树节点ID
     */
    public int addChildrenFolders2DTree(File file, int parentTreeTd, Dtree dtree, TreeConfig treeConfig, FlyContext flyContext, FileFilter filePredicate) {
        if (treeConfig.isAddParent()) {
            DtreeObj obj = dtree.addDtreeObj(parentTreeTd, file.getName(), treeConfig.getFolderUrl() + URLEncoder.encode(file.getAbsolutePath(), StandardCharsets.UTF_8));
            parentTreeTd = obj.getId();
        }
        /**处理文件夹*/
        BiConsumer<DtreeObj, File> floderConsumer = (dtreeObj, cfile) -> {
            String url = "/article/articles.do?buId=lastEdit&rootPath=" + URLEncoder.encode(cfile.getAbsolutePath(), StandardCharsets.UTF_8);
            if (treeConfig.getFolderUrl() != null) {
                url = treeConfig.getFolderUrl() + URLEncoder.encode(cfile.getAbsolutePath(), StandardCharsets.UTF_8);
            }
            dtreeObj.setUrl(url);
        };
        /**处理文件*/
        BiConsumer<DtreeObj, File> fileConsumer = (dtreeObj, cfile) -> {
            dtreeObj.setUrl(treeConfig.getFileUrl() + URLEncoder.encode(cfile.getAbsolutePath(), StandardCharsets.UTF_8));
            dtreeObj.setTarget(treeConfig.getTarget());

            Pair<String, String> pair = treeConfig.getIcon(cfile);
            if (pair != null) {
                dtreeObj.setIcon(pair.getLeft());
                dtreeObj.setIconOpen(pair.getRight());
            }
            for (ArticleTreeSPI spi : articleTreeMap.get("childArticleLine")) {
                ArticleTreeSpiParam param = new ArticleTreeSpiParam(flyContext, dtree, cfile);
                param.setTreeConfig(treeConfig);
                param.setDtreeObj(dtreeObj);
                spi.buildTree(param, this);
            }

        };
        return dtree.addFoldersWithChildren(file, parentTreeTd, floderConsumer, fileConsumer, filePredicate, treeConfig.isDelEmpty(), flyContext);
    }


    public Dtree getTree4ViewArticle(FlyContext flyContext, String noteFileStr, String sceneName) {
        File file = new File(noteFileStr);

        Dtree dtree = new Dtree();
        dtree.addRoot("目录", "");
        for (ArticleTreeSPI spi : articleTreeMap.get("viewArticle")) {
            ArticleTreeSpiParam param = new ArticleTreeSpiParam(flyContext, dtree, file)
                    .setSceneName(sceneName);
            spi.buildTree(param, this);
        }
        return dtree;
    }


    private String getBrowserUrl(File file) {
        if (file.isDirectory()) {
            return "/browser/list.do?sort=1&currDir=" + URLEncoder.encode(file.getAbsolutePath(), StandardCharsets.UTF_8);
        } else {
            return "/viewFile?absolutePath=" + URLEncoder.encode(file.getAbsolutePath(), StandardCharsets.UTF_8);
        }
    }

    public List<DtreeObj> getDtree4Browser(FlyContext flyContext, List<Path> paths) {
        //设置文件导航树
        //设置目录
        Dtree dtree = new Dtree();
        dtree.addRoot("文件管理", "");
        for (Path path : paths) {
            File file = path.toFile();
            //顶层节点
            DtreeObj dtreeObj = dtree.addDtreeObjToRoot(file.getName(), getBrowserUrl(file));

            //子目录加入
            FileFilter fileFilter = predicateMap.get("filePredicate4Browser");
            this.addChildrenFolders2DTree(file, dtreeObj.getId(), dtree, treeConfigMap.get("browser"), flyContext, fileFilter);
        }


        return dtree.getDtreeObjs();
    }

    public List<DtreeObj> getDtreeObjs4Articles(FlyContext flyContext) {
        //顶层节点, 该图标名称，不展示图标
        Dtree dtree = new Dtree("目录", "");

        //默认文章树
        Path path = pathService.getUserDir(flyContext.getPin());
        FileFilter fileFilter = predicateMap.get("treeFilePredicate");
        this.addChildrenFolders2DTree(path.toFile(), dtree.getRoot().getId(), dtree, this.treeConfigMap.get("articles"), flyContext, fileFilter);

        return dtree.getDtreeObjs();
    }

    public void setPathService(PathService pathService) {
        this.pathService = pathService;
    }

    public void setArticleLoad(ArticleLoad articleLoad) {
        this.articleLoad = articleLoad;
    }

    public void setTreeConfigMap(Map<String, TreeConfig> treeConfigMap) {
        this.treeConfigMap = treeConfigMap;
    }

    public void setArticleTreeMap(Map<String, List<ArticleTreeSPI>> articleTreeMap) {
        this.articleTreeMap = articleTreeMap;
    }

    public Map<String, TreeConfig> getTreeConfigMap() {
        return treeConfigMap;
    }
}
