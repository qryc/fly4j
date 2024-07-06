package farticle.domain.consts;

import java.util.HashSet;
import java.util.Set;

public class FlyConst {


    /// workspace类型的值
    public static final String W_TYPE_FLY = "fly";
    public static final String W_TYPE_FILE = "file";

    /// 博客组织-工作路径
    public static final String ORGANIZE_WORKPATH = "workPath";
    public static final String WORKPATH_GITHUB = "gitHub";
    public static final String WORKPATH_DRAFT = "draft";
    public static final String WORKPATH_ARTICLE = "article";

    //客户端配置
    //md编辑器
    public static final String CLIENT_MDEDITOR = "mdEditor";
    public static final String CLIENT_MDEDITOR_TEXT = "1";//文本编辑器
    public static final String CLIENT_MDEDITOR_CK = "2";//ckEditor


    /**
     * 场景定义
     */
    //全文搜索
    public static final String SCENE_GLOBAL_SEARCH = "globalSearch";


    /**
     * ACConst ArticleComponentConst
     * Created by qryc on 2019/12/14.
     */
    public static class ACConst {
        /**
         * AEM(ArticleExtMap)
         */
        public static final String AEM_MARK = "mark";
        //因子知父，父不自知，故以此标识,null为子
        public static final String AEM_ALBUM = "parent";
        public static final String AEM_HREF_TITLE = "hrefTitle";

        /**
         * 业务身份 BUID_AQ_(businessIdentityArticleQuery)
         * 业务身份对应功能列表，可以配置，程序核心框架不耦合业务身份，框架关心SPI以及如何调度SPI
         */
        public static final String BAQ_LAST_EDIT = "lastEdit";
        public static final String BAQ_ALL = "articlesQueryAll";

    }

    public static Set<String> workNotDisplayFileNames = new HashSet<>();

    static {
        workNotDisplayFileNames.add("91 个人空间");
        workNotDisplayFileNames.add("92 架构验证-FLY");
        workNotDisplayFileNames.add("93 本地设计");


    }
}
