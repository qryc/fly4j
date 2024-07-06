package fnote.article.maintain.impl;

import farticle.domain.entity.*;
import farticle.domain.extension.modify.ArticleOrganizeFilter;
import farticle.domain.infrastructure.ArticleRepository;
import fly4j.common.crypto.AESUtil;
import fly4j.common.event.AlterEvent;
import fly4j.common.util.DateUtil;
import fly4j.common.util.FlyPreconditions;
import fly4j.common.util.RepositoryException;
import fnote.article.maintain.ArticleMaintain;
import fnote.common.event.EventCenter;
import fnote.user.domain.entity.IdPin;
import fnote.user.domain.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author qryc on 2020/08/15.
 */
public class ArticleMaintainImpl implements ArticleMaintain {
    private static final Logger log = LoggerFactory.getLogger(ArticleMaintainImpl.class);
    private ArticleRepository articleRepository;
    private UserService userService;
    private EventCenter eventCenter;
    private List<ArticleOrganizeFilter> organizeFilters;

    public void sendEvent(AlterEvent alterEvent) {
        try {
            eventCenter.fire(alterEvent);
        } catch (Exception e) {
            log.error("alterEvent " + alterEvent, e);
        }
    }

    @Override
    public CplArticle addOrEditMdArticle(MdArticleParam mdArticleParam) throws RepositoryException {
        try {
            FlyPreconditions.requireNotEmpty(mdArticleParam.getMdContent(), "addOrEditMdArticle content is empty");
            if (StringUtils.isBlank(mdArticleParam.getNoteFileStr())) {
                return this.insertMdArticle(mdArticleParam);
            } else {
                return this.updateMdArticle(mdArticleParam);
            }
        } catch (RepositoryException e) {
            log.error("error", e);
        }
        return null;
    }

    @Override
    public CplArticle insertMdArticle(MdArticleParam mdArticleParam) throws RepositoryException {

        var contentParam = mdArticleParam.convert2ArticleContent();

        return insertArticle(mdArticleParam.getPin(), contentParam, WorkSpaceParam.of(mdArticleParam.getWorkspace()));
    }

    private CplArticle insertArticle(String pin, ContentParam contentParam, WorkSpaceParam workspace) throws RepositoryException {
        if (StringUtils.isBlank(workspace.spaceName())) {
            workspace = WorkSpaceParam.of(WorkSpaceParam.WORKSPACE_COMPANY);
        }
        //默认公开不加密
        var articleContent = ArticleContent.of(ArticleAuthEnum.REAL_OPEN, contentParam);

        //组织关系
        var organize = new ArticleOrganize()
                .setTextType(ArticleOrganize.TYPE_MD);
        //默认CK编辑器
        organize.setMdEditor(ArticleOrganize.ORGANIZE_MDEDITOR_CK);


        var cplArticle = new CplArticle(articleContent, organize)
                .setPin(pin)
                .setCreateTime(new Date());
        cplArticle.setExtId(System.currentTimeMillis());
        cplArticle.getArticleOrganize().setWorkspace(workspace.spaceName());
        if (!ArticleAuthEnum.REAL_OPEN.equals(articleContent.authEnum())) {
            cplArticle.encryptArticle(getMima(pin));
        }
        String fileStr = articleRepository.insertCplArticle(cplArticle);
        cplArticle.setNoteFileStr(fileStr);
        ArticleEventObj articleEventObj = new ArticleEventObj(cplArticle.getIdPin(), ArticleEventObj.INSERT).domain("article");
        this.sendEvent(new AlterEvent(articleEventObj));
        return cplArticle;
    }

    @Override
    public CplArticle insertRichArticle(RichArticleParam richArticleParam) throws RepositoryException {

        var articleContent = new ContentParam(richArticleParam.getTitle(), richArticleParam.getContent());
        return insertArticle(richArticleParam.getPin(), articleContent, WorkSpaceParam.of(richArticleParam.getWorkspace()));
    }

    @Override
    public void appendFrontOfContent(final IdPin idPin, String appendStr, String encryptPwd) throws RepositoryException {
        FlyPreconditions.requireNotEmpty(idPin.getNoteFileStr());
        FlyPreconditions.requireNotEmpty(appendStr);
        var cplArticle = this.getCplArticleDecrypt(idPin, encryptPwd);
        cplArticle.appendFrontOfContent(idPin, appendStr);
        //只读模式添加标志
        if (cplArticle.getArticleOrganize().getEditMode() == 1) {
            cplArticle.appendFrontOfContent(idPin, "<span style='background-color: #D9D9D9 ;padding:5px'>%s锁定后追加，等待合并!</span>".formatted(DateUtil.getCurrDateStr()));
        }
        cplArticle.encryptArticle(getMima(idPin.getPin()));
        articleRepository.updateCplArticle(cplArticle);
        ArticleEventObj articleEventObj = new ArticleEventObj(idPin, ArticleEventObj.EDIT).domain("article");
        this.sendEvent(new AlterEvent(articleEventObj));

    }

    @Override
    public CplArticle updateArticleOrganize(final IdPin idPin, ArticleOrganizeParam param) throws RepositoryException {

        FlyPreconditions.requireNotEmpty(idPin.getNoteFileStr(), "update article id can't be null");
        ArticleOrganize organize = BeanConvert.organizeParam2Orgnize(param);

        //取出并更新,不解密博客内容
        var cplArticle = this.getCplArticleDecrypt(idPin, getMima(idPin.getPin()));
        ArticleContent articleContent = cplArticle.getArticleContent();

        //调用扩展点执行扩展逻辑
        if (null != organizeFilters) {
            for (var filter : organizeFilters) {
                //集合过滤器，为了整体能编排，使用的集合
                cplArticle = filter.filterOrganize(param, cplArticle);
            }
        }

        cplArticle.organizeArticle(organize, idPin);
        if (ArticleAuthEnum.REAL_OPEN.equalsCode(param.getOpen())) {
            cplArticle.alterAuthEnum(param.getAuthEnum());
        } else {
            //加密
            var contentEncrypt = AESUtil.encryptStr2HexStr(articleContent.content(), getMima(idPin.getPin()));
            cplArticle.setArticleContent(new ArticleContent(param.getAuthEnum(), articleContent.title(), contentEncrypt));
        }


        articleRepository.updateCplArticle(cplArticle);
        ArticleEventObj articleEventObj = new ArticleEventObj(idPin, ArticleEventObj.EDIT).domain("articleOrgnize");
        this.sendEvent(new AlterEvent(articleEventObj));
        return cplArticle;
    }


    @Override
    public CplArticle updateMdArticle(MdArticleParam mdArticleParam) throws RepositoryException {
        //非空判断
        Objects.requireNonNull(mdArticleParam.getNoteFileStr(), "update article id can't be null");
        FlyPreconditions.requireNotEmpty(mdArticleParam.getPin(), "loginUser pin  is empty" + mdArticleParam.getNoteFileStr());
        ContentParam contentParam = mdArticleParam.convert2ArticleContent();
        FlyPreconditions.requireNotEmpty(contentParam.content(), "article content is empty" + mdArticleParam.getNoteFileStr());

        //构造需要持久化的新内存对象
        var idPin = IdPin.of(mdArticleParam.getPin(), mdArticleParam.getNoteFileStr());
        final var cplArticle = this.getCplArticleEncrypt(idPin, WorkSpaceParam.of(mdArticleParam.getWorkspace()));
        updateArticleContent(cplArticle, contentParam, mdArticleParam.getPin(), idPin);
        return cplArticle;
    }

    private void updateArticleContent(CplArticle cplArticle, ContentParam contentParam, String pin, IdPin idPin) throws RepositoryException {
        cplArticle.setModifyTime(new Date());
        //判断是否加密,入参都是不加密的
        if (ArticleAuthEnum.REAL_OPEN.equals(cplArticle.getArticleContent().authEnum())) {
            cplArticle.setArticleContent(cplArticle.getArticleContent().of(contentParam));
        } else {
            //加密
            var contentEncrypt = AESUtil.encryptStr2HexStr(contentParam.content(), getMima(pin));
            cplArticle.setArticleContent(cplArticle.getArticleContent().of(contentParam.title(), contentEncrypt));
        }
        articleRepository.updateCplArticle(cplArticle);
        ArticleEventObj articleEventObj = new ArticleEventObj(idPin, ArticleEventObj.EDIT).domain("article");
        this.sendEvent(new AlterEvent(articleEventObj));
    }

    private String getMima(String pin) throws RepositoryException {
        return userService.getUserConfig(pin).getMima();
    }

    @Override
    public CplArticle updateRichArticle(RichArticleParam richArticleParam, WorkSpaceParam workSpaceParam) throws RepositoryException {
        Objects.requireNonNull(richArticleParam.getNoteFileStr(), "update article id can't be null");
        FlyPreconditions.requireNotEmpty(richArticleParam.getPin(), "loginUser pin  is empty" + richArticleParam.getNoteFileStr());
        FlyPreconditions.requireNotEmpty(richArticleParam.getContent(), "article content is empty" + richArticleParam.getNoteFileStr());

        //构造需要持久化的新内存对象
        var idPin = IdPin.of(richArticleParam.getPin(), richArticleParam.getNoteFileStr());
        final var cplArticle = this.getCplArticleEncrypt(idPin, workSpaceParam);
        var articleContent = new ContentParam(richArticleParam.getTitle(), richArticleParam.getContent());
        updateArticleContent(cplArticle, articleContent, richArticleParam.getPin(), idPin);
        return cplArticle;
    }


    @Override
    public void deleteArticleById(IdPin idPin, WorkSpaceParam workSpaceParam) throws RepositoryException {
        var cplArticle = this.getCplArticleEncrypt(idPin, workSpaceParam);
        if (cplArticle.canDel()) {
            articleRepository.deleteCplArticleById(idPin);
            ArticleEventObj articleEventObj = new ArticleEventObj(idPin, ArticleEventObj.DELETE).domain("article");
            this.sendEvent(new AlterEvent(articleEventObj));
        }
    }

    @Override
    public CplArticle getCplArticleDecrypt(IdPin idPin, String encryptPwd) throws RepositoryException {
        var cplArticle = articleRepository.getCplArticleById4Edit(idPin);
        return cplArticle.decryptArticle(encryptPwd);
    }

    public CplArticle getCplArticleEncrypt(IdPin idPin, WorkSpaceParam workSpaceParam) throws RepositoryException {
        return articleRepository.getCplArticleById4Edit(idPin);
    }

    public void setArticleRepository(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setEventCenter(EventCenter eventCenter) {
        this.eventCenter = eventCenter;
    }

    public void setOrganizeFilters(List<ArticleOrganizeFilter> organizeFilters) {
        this.organizeFilters = organizeFilters;
    }

}
