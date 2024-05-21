package fnote.article.web.controller;

import farticle.domain.consts.FlyConst;
import farticle.domain.entity.*;
import fnote.article.maintain.ArticleMaintain;
import fnote.article.share.AuthShareService;
import fnote.article.maintain.DraftService;
import fnote.domain.config.FlyContext;
import fnote.domain.config.FlyContextFacade;
import flynote.application.manual.ManualService;
import farticle.domain.view.TreeService;
import fnote.user.domain.entity.IdPin;
import fly4j.common.util.DateUtil;
import fly4j.common.util.FlyPreconditions;
import fly4j.common.http.CookiesUtil;
import fly4j.common.http.WebUtil;
import fly4j.common.util.RepositoryException;
import farticle.domain.entity.WorkSpaceParam;
import fnote.web.controller.MenuController;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 博客管理，会改变数据
 * 读写分离
 *
 * @author qryc
 */
@Controller
@RequestMapping("/articleMaintain")
public class ArticleMaintainController extends MenuController {
    @Resource
    private ArticleMaintain articleMaintain;
    @Resource
    private ManualService manual;
    @Resource
    private AuthShareService authShareService;
    @Resource
    private FlyContextFacade flyContextFacade;
    @Resource
    private TreeService dtreeUtil;

    @RequestMapping(value = "addBlog4Md.do")
    public String addOrEditMdArticle(HttpServletRequest req,
                                     HttpServletResponse resp, ModelMap context) throws RepositoryException {
        var loginUser = flyContextFacade.getLoginUser(req);
        Objects.requireNonNull(loginUser, "guest can't addOrEditMdArticle");
        MdArticleParam mdArticleParam = new MdArticleParam()
                .setNoteFileStr(getNoteFileStrFromReq(req))
                .setMdContent(req.getParameter("mdContent"))
                .setPin(loginUser.pin());


        if (WorkSpaceParam.WORKSPACE_PERSON.equals(flyContextFacade.getClientConfig(req, resp).getWorkspace())) {
            mdArticleParam.setWorkspace(WorkSpaceParam.WORKSPACE_PERSON);
        } else {
            mdArticleParam.setWorkspace(WorkSpaceParam.WORKSPACE_COMPANY);
        }
        CplArticle cplArticle = articleMaintain.addOrEditMdArticle(mdArticleParam);

        //保险柜直接调整浏览页面
        if (cplArticle.getArticleContent().isInsurance()) {
            return "redirect:/article/viewArticle.do?noteFileStr=%s".formatted(NoteFileStrUtil.encode(cplArticle.getNoteFileStr()));
        } else {
//            return "redirect:/article/articles.do?buId=lastEdit";
            return "redirect:" + TreeService.editUrl + cplArticle.getEncodeNoteFileStr();
        }
    }

    public String getNoteFileStrFromReq(HttpServletRequest req) {
        return URLDecoder.decode(WebUtil.getParameterStr(req, "noteFileStr"), StandardCharsets.UTF_8);
    }

    private WorkSpaceParam getWorkSpace(HttpServletRequest req, HttpServletResponse resp) {
        return WorkSpaceParam.of(flyContextFacade.getClientConfig(req, resp).getWorkspace());
    }

    @RequestMapping(value = "organizeArticle.do")
    public String organizeArticle(ArticleOrganizeParam organizeParam, HttpServletRequest req,
                                  HttpServletResponse resp, ModelMap context) throws RepositoryException {
        var flyContext = flyContextFacade.getFlyContext(req, resp);
        Objects.requireNonNull(flyContext.loginUser(), "guest can't organizeArticle");
        var noteFileStr = getNoteFileStrFromReq(req);
        CplArticle cplArticle = articleMaintain.updateArticleOrganize(IdPin.of(flyContext.getPin(), noteFileStr), organizeParam);
        return "redirect:/articleMaintain/toOrganizeBlog.do?noteFileStr=" + URLEncoder.encode(cplArticle.getNoteFileStr(), StandardCharsets.UTF_8);
    }

    @RequestMapping(value = "append.do")
    public String append(String appendPre, HttpServletRequest req,
                         HttpServletResponse resp, ModelMap context) throws RepositoryException {
        var flyContext = flyContextFacade.getFlyContext(req, resp);
        Objects.requireNonNull(flyContext.loginUser(), "guest can't append");
        var idPin = IdPin.of(flyContext.getPin(), getNoteFileStrFromReq(req));
        articleMaintain.appendFrontOfContent(idPin, appendPre, flyContext.userConfig().getMima());
        // sleep for unsync
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "redirect:/article/viewArticle.do?noteFileStr=%s&append=true".formatted(NoteFileStrUtil.encode(getNoteFileStrFromReq(req)));
    }


    /**
     * 分享博客
     */
    @RequestMapping(value = "shareBlog.do")
    public String shareArticle(Long id, HttpServletRequest req,
                               HttpServletResponse resp, ModelMap context) throws RepositoryException {
        FlyPreconditions.requireNotEmpty(id, "shared fail! id is null");
        var flyContext = flyContextFacade.getFlyContext(req, resp);
        Objects.requireNonNull(flyContext.loginUser(), "guest can't shared");
        //生成共享码
        WorkSpaceParam workSpaceParam = getWorkSpace(req, resp);
        authShareService.shareArticle(id, flyContext.getPin(), flyContext.getEncryptPwd(), workSpaceParam);
        return "redirect:/article/viewArticle.do?noteFileStr=" + id;

    }


    @RequestMapping(value = "editRichText.do")
    public String editRichText(RichArticleParam richArticleParam, HttpServletRequest req,
                               HttpServletResponse resp, ModelMap context) throws RepositoryException {
        CplArticle cplArticle = doEditRichText(richArticleParam, req, resp);
        // sleep for unsync
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "redirect:" + TreeService.editUrl + cplArticle.getEncodeNoteFileStr();
//        return "redirect:/article/articles.do?buId=lastEdit";

    }

    private CplArticle doEditRichText(RichArticleParam richArticleParam, HttpServletRequest req, HttpServletResponse resp) throws RepositoryException {
        var flyContext = flyContextFacade.getFlyContext(req, resp);
        Objects.requireNonNull(flyContext.loginUser(), "guest can't editorBlog");
        FlyPreconditions.requireNotEmpty(richArticleParam.getNoteFileStr(), "id can't be null");
        richArticleParam.setNoteFileStr(URLDecoder.decode(richArticleParam.getNoteFileStr(), StandardCharsets.UTF_8));
        richArticleParam.setPin(flyContext.getPin());
        WorkSpaceParam workSpaceParam = getWorkSpace(req, resp);
        return articleMaintain.updateRichArticle(richArticleParam, workSpaceParam);
    }

    @RequestMapping(value = "ajaxSaveRichText.do")
    public void ajaxSaveRichText(RichArticleParam richArticleParam, HttpServletRequest req,
                                 HttpServletResponse resp, ModelMap context) throws RepositoryException {
        var flyContext = flyContextFacade.getFlyContext(req, resp);
        Objects.requireNonNull(flyContext.loginUser(), "guest can't append");
        PrintWriter writer = null;
        resp.setCharacterEncoding("utf-8");
        resp.setContentType("text/html; charset=utf-8");
        boolean ajax = "XMLHttpRequest".equals(req.getHeader("X-Requested-With"));

        FlyPreconditions.checkStateTrue(!ajax, "not Ajax request");
        richArticleParam.setNoteFileStr(URLDecoder.decode(richArticleParam.getNoteFileStr(), StandardCharsets.UTF_8));
        //	System.out.println("ajax"+ajax+req.getHeader("X-Requested-With"));
        try {
            writer = resp.getWriter();

            doEditRichText(richArticleParam, req, resp);
            writer.write("保存成功！");
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /**
     * 异步添加博客
     */
    @RequestMapping(value = "ajaxSave.do")
    public void ajaxSave(MdArticleParam mdArticleParam, HttpServletRequest req,
                         HttpServletResponse resp, ModelMap context) throws RepositoryException {
        var flyContext = flyContextFacade.getFlyContext(req, resp);
        Objects.requireNonNull(flyContext.loginUser(), "guest can't append");
        PrintWriter writer = null;
        resp.setCharacterEncoding("utf-8");
        resp.setContentType("text/html; charset=utf-8");
        boolean ajax = "XMLHttpRequest".equals(req.getHeader("X-Requested-With"));
        mdArticleParam.setNoteFileStr(URLDecoder.decode(mdArticleParam.getNoteFileStr(), StandardCharsets.UTF_8));
        FlyPreconditions.checkStateTrue(!ajax, "not Ajax request");
        //	System.out.println("ajax"+ajax+req.getHeader("X-Requested-With"));
        try {
            writer = resp.getWriter();
            var msg = new StringBuilder();
            msg.append(DateUtil.getDateStr(new Date()));

            if (StringUtils.isNotBlank(mdArticleParam.getMdContent())) {
                try {
                    DraftService.setDraft(flyContext.getPin(), mdArticleParam.getMdContent());
                    msg.append("保存成功！");
                } catch (Exception e) {
                    msg.append(e.getMessage()).append(" ");
                }
            }
            writer.write(msg.toString());
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    /**
     * 到添加博客
     */
    @RequestMapping(value = "toAddBlog.do")
    public String toAddMdArticle(HttpServletRequest req,
                                 HttpServletResponse resp, ModelMap modelMap) throws RepositoryException {
        var flyContext = flyContextFacade.getFlyContext(req, resp);
        //权限判定
        Objects.requireNonNull(flyContext.loginUser(), "guest can't append");

        modelMap.put("editor", 1);//默认使用markdown
        modelMap.put("addArticleManual", manual.getAddArticleManual());
        modelMap.put("currLocation", "toAddMdArticle");
        modelMap.put("rootPath", FlyContext.getCurrentWorkRootPath(flyContext.getPin()));
        modelMap.put("add", true);

        //设置返回页
        CookiesUtil.addHttpOnlyCookieEncode(req, resp, "edithome",
                req.getHeader("Referer"), 10000000);
        String mdeditor = flyContext.clientConfig().getMdEditor();
        if (FlyConst.CLIENT_MDEDITOR_CK.equals(mdeditor)) {
            return "article/editCkMdArticle";
        } else {
            return "article/editMdArticle";
        }

    }

    /**
     * 到添加和修改博客
     */
    @RequestMapping(value = "toEditArticle.do")
    public String toEditArticle(String noteFileStr, HttpServletRequest req,
                                HttpServletResponse resp, ModelMap modelMap) throws RepositoryException {
        var flyContext = flyContextFacade.getFlyContext(req, resp);
        //权限判定
        FlyPreconditions.requireNotEmpty(flyContext.loginUser(), "guest can't append");
        WorkSpaceParam workSpaceParam = getWorkSpace(req, resp);

        // 如果是修改，加载修改内容
        noteFileStr = URLDecoder.decode(noteFileStr, StandardCharsets.UTF_8);
        var cplArticle = articleMaintain.getCplArticleDecrypt(IdPin.of(flyContext.getPin(), noteFileStr), flyContext.userConfig().getMima());

        //设置用户当前工作空间
        File file = new File(noteFileStr);
        File parentFile = file.getParentFile();
        flyContext.setCurrentWorkRootPath(parentFile.getAbsolutePath());
        modelMap.put("dtreeObjs", dtreeUtil.getTree4ViewArticle(flyContext, noteFileStr, "editArticle"));
        PicCache.cachePic(parentFile);

        //路由兼容富文本编辑器
        if (cplArticle.getArticleOrganize().isTextType(ArticleOrganize.TYPE_HTML)) {

            // 设置手机
            var mobileSite = WebUtil.isFromPhone(req);
            // 如果是修改，加载修改内容
            //转义为了防止影响mce的展示,删除测试影响
            //  cplArticle.encodeHtml();
            // 下载时直接使用
            modelMap.put("cplArticle", cplArticle);
//            cplArticle.getArticleContent().replaceContentGtLt();
            System.out.println(cplArticle.getArticleContent().content());

            if (!mobileSite) {
                modelMap.put("ajaxSave", flyContext.userConfig().autoSave);
            } else {
                modelMap.put("ajaxSave", false);
            }
            modelMap.put("ajaxSave", false);
            //展示图片
//        Collection<File> files = FileUtil.listDirFiles(NetDiskConfig.getPicDir(flyContext.getPin()) + "/" + article.getId());
//        modelMap.put("fileInfos", FileInfoUtil.getFileInfos(files));

            modelMap.put("currLocation", "修改博客");

            modelMap.put("blogAuthEnums", ArticleAuthEnum.values());
            //设置返回页
            CookiesUtil.addHttpOnlyCookieEncode(req, resp, "edithome",
                    req.getHeader("Referer"), 10000000);
            return "article/editRichText";
        } else {
            var alterBlogParam = MdArticleParam.convert2MdParam(cplArticle);
            modelMap.put("alterBlogParam", alterBlogParam);


            //设定自动保存
            if (WebUtil.isFromPc(req)) {
                modelMap.put("ajaxSave", flyContext.userConfig().autoSave);
            } else {
                modelMap.put("ajaxSave", false);
            }

            //展示图片
//        Collection<File> files = FileUtil.listDirFiles(NetDiskConfig.getPicDir(flyContext.getPin()) + "/" + article.getId());
//        modelMap.put("fileInfos", FileInfoUtil.getFileInfos(files));

            modelMap.put("currLocation", "toEditArticle");
            //设置返回页
            CookiesUtil.addHttpOnlyCookieEncode(req, resp, "edithome",
                    req.getHeader("Referer"), 10000000);

            // sleep for unsync
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            modelMap.put("menu", getMenu(req, flyContext));
            //图片缓存
            String mdEditorSetOne = cplArticle.getArticleOrganize().getMdEditor();
            if (StringUtils.isBlank(mdEditorSetOne) || ArticleOrganize.ORGANIZE_DEFAULT.equals(mdEditorSetOne)) {
                //走客户端cookies设置
                String mdeditor = flyContext.clientConfig().getMdEditor();
                if (FlyConst.CLIENT_MDEDITOR_CK.equals(mdeditor)) {
                    return "article/editCkMdArticle";
                } else {
                    return "article/editMdArticle";
                }
            } else {
                //走文章组织设置
                if (ArticleOrganize.ORGANIZE_MDEDITOR_CK.equals(mdEditorSetOne)) {
                    return "article/editCkMdArticle";
                } else {
                    return "article/editMdArticle";
                }
            }
        }


    }

    @RequestMapping(value = "toViewSourceCode.do")
    public String toViewSourceCode(String noteFileStr, HttpServletRequest req,
                                   HttpServletResponse resp, ModelMap modelMap) throws RepositoryException {
        var flyContext = flyContextFacade.getFlyContext(req, resp);
        //权限判定
        FlyPreconditions.requireNotEmpty(flyContext.loginUser(), "guest can't append");
        WorkSpaceParam workSpaceParam = getWorkSpace(req, resp);

        // 如果是修改，加载修改内容
        noteFileStr = URLDecoder.decode(noteFileStr, StandardCharsets.UTF_8);
        var cplArticle = articleMaintain.getCplArticleDecrypt(IdPin.of(flyContext.getPin(), noteFileStr), flyContext.userConfig().getMima());
        var alterBlogParam = MdArticleParam.convert2MdParam(cplArticle);
        //替换图床
        alterBlogParam.setMdContent(alterBlogParam.getMdContent().replaceAll("https://raw.githubusercontent.com/qryc/pic/master/", "http://fly4j.cn/PicBed/"));
        modelMap.put("alterBlogParam", alterBlogParam);

        return "article/viewSourceCode";


    }

    /**
     * 到添加和修改博客
     */
    @RequestMapping(value = "del.do")
    public String del(String noteFileStr, HttpServletRequest req,
                      HttpServletResponse resp, ModelMap modelMap) throws RepositoryException {
        var flyContext = flyContextFacade.getFlyContext(req, resp);
        noteFileStr = URLDecoder.decode(noteFileStr, StandardCharsets.UTF_8);
        //权限判定
        Objects.requireNonNull(flyContext.loginUser(), "guest can't append");
        WorkSpaceParam workSpaceParam = getWorkSpace(req, resp);
        articleMaintain.deleteArticleById(IdPin.of(flyContext.getPin(), noteFileStr), workSpaceParam);

        return "redirect:/article/articles.do?buId=lastEdit";
    }


    /**
     * 到添加和修改博客
     */
    @RequestMapping(value = "toOrganizeBlog.do")
    public String toOrganizeArticle(String noteFileStr, HttpServletRequest req,
                                    HttpServletResponse resp, ModelMap modelMap) throws RepositoryException {
        var flyContext = flyContextFacade.getFlyContext(req, resp);
        Objects.requireNonNull(flyContext.loginUser(), "guest can't append");
        noteFileStr = URLDecoder.decode(noteFileStr, StandardCharsets.UTF_8);
        // 设置手机
        var mobileSite = WebUtil.isFromPhone(req);
        // 如果是修改，加载修改内容
        FlyPreconditions.requireNotEmpty(noteFileStr, "id can't be null");
        var cplArticle = articleMaintain.getCplArticleDecrypt(IdPin.of(flyContext.getPin(), noteFileStr), flyContext.getEncryptPwd());
        modelMap.put("cplArticle", cplArticle);
        ArticleOrganizeParam organizeParam = BeanConvert.organize2Param(cplArticle);
        modelMap.put("organize", organizeParam);

        if (!mobileSite) {
            modelMap.put("ajaxSave", flyContext.userConfig().autoSave);
        } else {
            modelMap.put("ajaxSave", false);
        }
        modelMap.put("editor", cplArticle.getArticleOrganize().getTextType());
        //展示图片
//        Collection<File> files = FileUtil.listDirFiles(NetDiskConfig.getPicDir(flyContext.getPin()) + "/" + cplArticle.getArticle().getId());
//        modelMap.put("fileInfos", FileInfoUtil.getFileInfos(files));

        modelMap.put("currLocation", "修改博客");

        modelMap.put("blogAuthEnums", ArticleAuthEnum.values());
        //设置返回页
        CookiesUtil.addHttpOnlyCookieEncode(req, resp, "edithome",
                req.getHeader("Referer"), 10000000);
        return "article/organizeArticle";
    }


}
