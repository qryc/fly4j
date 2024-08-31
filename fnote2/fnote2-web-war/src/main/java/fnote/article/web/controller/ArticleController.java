package fnote.article.web.controller;

import farticle.domain.consts.FlyConst;
import farticle.domain.entity.ArticleAuthEnum;
import farticle.domain.entity.CplArticle;
import farticle.domain.entity.DtreeObj;
import farticle.domain.extension.query.ArticleQueryParam;
import farticle.domain.extension.query.QueryBuilder;
import farticle.domain.view.*;
import fly.application.git.GitService;
import fly4j.common.http.WebUtil;
import fly4j.common.track.TrackContext;
import fly4j.common.util.RandomUtil;
import fly4j.common.util.RepositoryException;
import flynote.applicaion.service.TreeCache;
import flynote.application.manual.ManualService;
import flynote.article.query.ArticleQuery;
import fnote.article.maintain.DraftService;
import fnote.article.share.AuthShareService;
import fnote.domain.config.FlyContext;
import fnote.domain.config.FlyContextFacade;
import fnote.user.domain.entity.IdPin;
import fnote.user.domain.service.UserService;
import fnote.web.controller.ArticleMenuService;
import fnote.web.controller.IndexController;
import fnote.web.controller.MenuController;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static fnote.web.common.FlyWebUtil.SMVC_REDIRECT_URL;

/**
 * 博客查看控制器
 *
 * @author qryc
 */
@Controller
@RequestMapping("/article")
public class ArticleController extends MenuController {
    private static final Logger log = LoggerFactory.getLogger(ArticleController.class);
    @Resource
    private ArticleQuery articleQuery;
    @Resource
    private ArticleViewQuery articleViewQuery;
    @Resource
    private ManualService manual;
    @Resource
    private UserService userService;
    @Resource
    private AuthShareService authShareService;
    @Resource
    private FlyContextFacade flyContextFacade;
    @Resource
    private TreeService dtreeUtil;

    private static String tokenName = "mima";


    /**
     * 查看最近修改的博客列表
     */
    @GetMapping(value = "articles.do")
    public String articles(HttpServletRequest req, HttpServletResponse resp, ModelMap context) throws RepositoryException {
        try {
            /**获取入参**/
            String buId = req.getParameter("buId");
            String title = req.getParameter("title");
            String rootPath = req.getParameter("rootPath");
            Integer maturity = WebUtil.getParameterInt(req, "maturity", null);

            /** 网站地图隐藏入口，并且输入为空，跳转网站地图页面 */
            if (FlyConst.SCENE_GLOBAL_SEARCH.equals(buId)
                    && StringUtils.isBlank(title)) {
                return "redirect:/index/webSiteMap.do";
            }

            /** 构造文章查询参数 */
            FlyContext flyContext = flyContextFacade.getFlyContext(req, resp);
            ArticleQueryParam queryParam = QueryBuilder.newBuilder().flyContext(flyContext).buId(buId)
                    .rootPath(rootPath).showMaturity(maturity).searchTitle(title).buildArticleQuery();


            /** 文章列表查询 */
            List<ArticleView4List> list = articleQuery.queryShortArticleViews(queryParam);
            context.put("list", list);

            /**导航树*/
            List<DtreeObj> dtreeObjs = TreeCache.getDtreeObjs(flyContext.getPin());
            if (null == dtreeObjs) {
                log.info("dtreeObjs get from File");
                dtreeObjs = dtreeUtil.getDtreeObjs4Articles(flyContext);
                TreeCache.putDtreeObjs(flyContext.getPin(), dtreeObjs);
            } else {
                log.info("dtreeObjs get from cache");
            }
            context.put("dtreeObjs", dtreeObjs);

            /**设置用户当前工作空间*/
            flyContext.setCurrentWorkRootPath(rootPath);

            /** 设置回传参数 */
            context.put("buId", buId);
            context.put("title", title);
            context.put("currLocation", "lastEditArticles");
            context.put("iknowInfo", TrackContext.getTrackInfo());
            setMenu(req, context, flyContext);
        } catch (RepositoryException e) {
            log.error("articles error!", e);
            throw e;
        }


        return "article/articleList";
    }

    @GetMapping(value = "genPdf.do")
    public String genPdf(HttpServletRequest req, HttpServletResponse resp, ModelMap context) throws RepositoryException {
        try {
            /**获取入参**/
            String buId = req.getParameter("buId");
            String title = req.getParameter("title");
            String rootPath = req.getParameter("rootPath");
            Integer maturity = WebUtil.getParameterInt(req, "maturity", null);

            /** 网站地图隐藏入口，并且输入为空，跳转网站地图页面 */
            if (FlyConst.SCENE_GLOBAL_SEARCH.equals(buId)
                    && StringUtils.isBlank(title)) {
                return "redirect:/index/webSiteMap.do";
            }

            /** 构造文章查询参数 */
            FlyContext flyContext = flyContextFacade.getFlyContext(req, resp);
            ArticleQueryParam queryParam = QueryBuilder.newBuilder().flyContext(flyContext).buId(buId)
                    .rootPath(rootPath).showMaturity(maturity).searchTitle(title).buildArticleQuery();


            /**导航树*/
            log.info("dtreeObjs get from File");
            dtreeUtil.getDtreeObjs4Articles(flyContext);

            /**设置用户当前工作空间*/
            flyContext.setCurrentWorkRootPath(rootPath);

            /** 设置回传参数 */
            context.put("buId", buId);
            context.put("title", title);
            context.put("currLocation", "lastEditArticles");
            context.put("iknowInfo", TrackContext.getTrackInfo());
            setMenu(req, context, flyContext);
        } catch (RepositoryException e) {
            log.error("genPdf error!", e);
            throw e;
        }


        return "article/articleList";
    }

    /**
     * 查看最近修改的博客列表
     */
    @GetMapping(value = "catalogTree.do")
    public String catalogTree(HttpServletRequest req, HttpServletResponse resp, ModelMap context) throws RepositoryException {
        try {
            /**获取入参**/
            String buId = req.getParameter("buId");
            String title = req.getParameter("title");
            String rootPath = req.getParameter("rootPath");


            /** 构造文章查询参数 */
            FlyContext flyContext = flyContextFacade.getFlyContext(req, resp);


            /**导航树*/
            context.put("dtreeObjs", dtreeUtil.getDtreeObjs4Articles(flyContext));

            /**设置用户当前工作空间*/
            flyContext.setCurrentWorkRootPath(rootPath);

            /** 设置回传参数 */
            context.put("buId", buId);
            context.put("title", title);
            context.put("currLocation", "catalogTree");
            context.put("iknowInfo", TrackContext.getTrackInfo());
        } catch (RepositoryException e) {
            log.error("catalogTree error!", e);
            throw e;
        }


        return "article/catalogTree";
    }

    @GetMapping(value = "viewArticlePri")
    public String viewArticlePri(HttpServletRequest req, HttpServletResponse resp, ModelMap context) throws RepositoryException {
        return viewArticle(req, resp, context);
    }


    @RequestMapping(value = "viewArticle.do")
    public String viewArticle(HttpServletRequest req, HttpServletResponse resp, ModelMap context) throws RepositoryException {
        var flyContext = flyContextFacade.getFlyContext(req, resp);
        if (null == flyContext.loginUser()) {
            return SMVC_REDIRECT_URL + "guest can not viewArticle";
        }

        //异步下载Git
        GitService.asynPullAndCommitGit("ArticleController-viewArticle");

        try {
            /** 参数校验 */
            var noteFileStr = req.getParameter("noteFileStr");
            var id = req.getParameter("id");
            if (StringUtils.isBlank(noteFileStr) && StringUtils.isBlank(id)) {
                throw new IllegalArgumentException("article noteFileStr and id is all null");
            }

            /** 文章查询 */
            ArticleView articleView = null;
            if (StringUtils.isNotBlank(noteFileStr)) {
                var filePathDecode = URLDecoder.decode(noteFileStr, StandardCharsets.UTF_8);
                articleView = articleViewQuery.getArticleViewWithFilter(IdPin.of(flyContext.getPin(), filePathDecode), flyContext);
                if (noteFileStr.endsWith("java")) {
                    context.put("viewCode", true);
                }
            } else {
                articleView = articleViewQuery.getArticleViewByIdWithFilter(flyContext, Long.parseLong(id));
            }

            // 构造菜单
            var articleViewMenu = ArticleMenuService.getArticleViewMenu(flyContext, articleView.getCplArticle());

            /** 构造页面展示对象 */
            ArticleInfoVo articleInfoVo = ArticleInfoVo.buildArticleInfoVo(articleView, articleViewMenu, flyContext);
            if (StringUtils.isNotBlank(noteFileStr) && noteFileStr.endsWith("java")) {
                articleInfoVo.setHtml(articleView.getCplArticle().getArticleContentStr());
            }
            context.put("articleInfoVo", articleInfoVo);


            //设置用户当前工作空间
            var articleDirFile = new File(articleView.getCplArticle().getNoteFileStr()).getParentFile();
            flyContext.setCurrentWorkRootPath(articleDirFile.getAbsolutePath());

            //展示同级目录树
            context.put("dtreeObjs", dtreeUtil.getTree4ViewArticle(flyContext, articleView.getCplArticle().getNoteFileStr(), "viewArticle").getDtreeObjs());

            /** 设置其他页面参数 */
            CplArticle cplArticle = articleView.getCplArticle();
            context.put("cplArticle", cplArticle);
            context.put("noteFileStr", cplArticle.getEncodeNoteFileStr());
            context.put("noteFileStrDecode", cplArticle.getNoteFileStr());
            context.put("canDel", cplArticle.canDel());

            /** 设置首页标志，首页标记控制不展示标题 */
            var reqURI = req.getRequestURI();
            var userHomePage = userService.getUserinfo(flyContext.getPin()).getUserConfig().getHomePage();
            if (StringUtils.isNotBlank(userHomePage) && reqURI.contains(userHomePage)) {
                context.put("homePage", true);
            }

            /** 私有文章接收密钥 */
            if (ArticleAuthEnum.ENCRYPT_PRI.equals(cplArticle.getArticleContent().authEnum())) {
                tokenName = RandomUtil.getRandomString(12);
                context.put("tokenName", tokenName);
            }


            /** 文章分享 */
            var idPin = IdPin.of(flyContext.getPin(), cplArticle.getNoteFileStr());
            context.put("shareBlogIdTtl", authShareService.getShareArticleInfo(idPin));

            //set append
            context.put("append", req.getParameter("append"));

            context.put("menuTopRight", articleInfoVo.getArticleViewMenu());
            /** 设置其它页面控制 */
            context.put("moreInfo", "");
            context.put("currLocation", "viewArticle");
            context.put("f_option", "view");
            context.put("iknowInfo", TrackContext.getTrackInfo());
            setMenu(req, context, flyContext);
        } catch (Exception e) {
            log.error("viewArticle", e);
            throw e;
        }
        return "article/viewArticle";

    }


    @RequestMapping(value = "viewManual.do")
    public String viewManual(HttpServletRequest req, HttpServletResponse resp, ModelMap context) {
        context.put("manual", manual.getManual());
        return "article/viewManual";
    }

    @RequestMapping(value = "viewDraft.do")
    public String viewDraft(HttpServletRequest req, HttpServletResponse resp, ModelMap context) throws RepositoryException {
        FlyContext flyContext = flyContextFacade.getFlyContext(req, resp);
        context.put("text", DraftService.getDraft(flyContext.getPin()));
        return "article/viewTextarea";
    }


}
