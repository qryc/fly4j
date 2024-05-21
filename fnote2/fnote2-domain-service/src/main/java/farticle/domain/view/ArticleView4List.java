package farticle.domain.view;

import farticle.domain.entity.ArticleContent;
import farticle.domain.entity.ArticleOrganize;
import fly4j.common.util.DateUtil;
import farticle.domain.entity.CplArticle;
import fnote.domain.config.FlyContext;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.Date;

public class ArticleView4List {
    CplArticle cplArticle;
    FlyContext flyContext;
    private static final String HREF_FORMAT = """
            <a  href=/article/viewArticle.do?noteFileStr=%s>
                %s
            </a>
            """;
    private static final String HREF_PRI_FORMAT_RED = """
            <font color='red'>
                <a style='color:red' href=/article/viewArticle.do?noteFileStr=%s>
                %s
                </a>
            </font>
            """;


    public ArticleView4List(CplArticle cplArticle, FlyContext flyContext) {
        ArticleContent content = cplArticle.getArticleContent();
        content.setTitle(content.title().replaceAll("#", "").trim());
        this.cplArticle = cplArticle;
        this.flyContext = flyContext;
    }


    public static Mark buildMark(CplArticle cplArticle) {
        Mark mark = new Mark();
        mark.appendMark(getLengthMark(cplArticle));
        if (cplArticle.getArticleOrganize().getMaturity() != null && cplArticle.getArticleOrganize().getMaturity() >= 999) {
            mark.appendMark("发表");
        }
        if (cplArticle.getArticleOrganize().getEditMode() == 1) {
            mark.appendMark("锁");
        } else if (cplArticle.getArticleOrganize().getEditMode() == 2) {
            mark.appendMark("封");
        }

        switch (cplArticle.getArticleContent().authEnum()) {
            case PRI -> mark.appendMark("私");
            case ENCRYPT_PUB -> mark.appendMark("秘");
            case ENCRYPT_PRI -> mark.appendMark("自定秘");
            case REAL_OPEN -> mark.appendMark("Plain");
        }
        if (!ArticleOrganize.WORKSPACE_COMPANY.equals(cplArticle.getArticleOrganize().getWorkspace())) {
            mark.appendMark("P");
        }
        if (cplArticle.getArticleContent().content().contains("锁定后追加，等待合并!")) {
            mark.appendMark("待合并");
        }
        if (cplArticle.getArticleOrganize().isTopping()) {
            mark.appendMark("顶");
        }
        if (cplArticle.getArticleOrganize().isInsurance()) {
            mark.appendMark("保");
        }
        return mark;

    }

    private static String getLengthMark(CplArticle cplArticle) {
        String content = cplArticle.getArticleContent().content();
        if (null == content || content.length() < 50) {
            return ("50");
        } else if (content.length() < 100) {
            return ("100");
        } else if (content.length() < 200) {
            return ("200");
        } else {
            return "";
        }
    }


    //lastEdit,album,alone,published
    public static String buildHrefTitle(CplArticle cplArticle) {

        StringBuilder titleView = new StringBuilder();
        titleView.append(buildHrefSingle(cplArticle));
        buildMark(cplArticle).getMarks().forEach(str -> {
            titleView.append(" <span class='badge badge-secondary'>%s</span>".formatted(str));
        });
        return titleView.toString();
    }

    protected static String buildHrefSingle(CplArticle cplArticle) {
        var title = cplArticle.getArticleContent().title();
        var href = "";
        switch (cplArticle.getArticleContent().authEnum()) {
            case ENCRYPT_PUB, PRI, ENCRYPT_PRI -> href = HREF_PRI_FORMAT_RED.formatted(URLEncoder.encode(cplArticle.getNoteFileStr(), StandardCharsets.UTF_8), title);
            case OPEN, REAL_OPEN -> href = HREF_FORMAT.formatted(URLEncoder.encode(cplArticle.getNoteFileStr(), StandardCharsets.UTF_8), title);
        }

        return href;
    }


    private static String buildArticleOperations(FlyContext flyContext, CplArticle cplArticle) {
        if (null == flyContext) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        if (cplArticle.getArticleOrganize().getEditMode() == ArticleOrganize.EDITMODE_LOCK) {

        } else {
            //正常模式通用功能链接
            if ("normal".equals(flyContext.clientConfig().getVm())) {
                if (cplArticle.getArticleOrganize().getEditMode() == ArticleOrganize.EDITMODE_EDIT) {//编辑功能
                    builder.append(" <a href='/articleMaintain/toEditArticle.do?edithome=list&noteFileStr=%s'>编辑</a>".formatted(cplArticle.getEncodeNoteFileStr()));
                }
            }
            //富文本编辑器
            if (cplArticle.getArticleOrganize().isTextType(ArticleOrganize.TYPE_HTML)) {
                //正常模式
                if ("normal".equals(flyContext.clientConfig().getVm())) {
                    if (cplArticle.getArticleOrganize().getEditMode() == ArticleOrganize.EDITMODE_APPEND) {//追加功能
                        builder.append("<a href='/article/viewArticle.do?noteFileStr=%s&append=true'>追加</a>".formatted(cplArticle.getEncodeNoteFileStr()));
                    }
                    builder.append("<a href='/articleMaintain/toOrganizeBlog.do?edithome=list&noteFileStr=%s'>组织</a>".formatted(cplArticle.getEncodeNoteFileStr()));
                } else {//极简模式
                    builder.append("<a href='/article/viewArticle.do?noteFileStr=%s&append=true' target='_self'>»追</a>".formatted(cplArticle.getEncodeNoteFileStr()));
                }
            }
            //MD编辑器
            if (cplArticle.getArticleOrganize().isTextType(ArticleOrganize.TYPE_MD)) {
                if ("normal".equals(flyContext.clientConfig().getVm())) {
                    if (cplArticle.getArticleOrganize().getEditMode() == ArticleOrganize.EDITMODE_APPEND) {//追加模式
                        builder.append("<a href='/article/viewArticle.do?noteFileStr=%s&append=true'>追加</a>".formatted(cplArticle.getEncodeNoteFileStr()));
                    }
                    builder.append("<a href='/articleMaintain/toOrganizeBlog.do?edithome=list&noteFileStr=%s'>组织</a>".formatted(cplArticle.getEncodeNoteFileStr()));
                } else {
                    if (cplArticle.getArticleOrganize().getEditMode() == ArticleOrganize.EDITMODE_EDIT) {
                        builder.append("<a href='/articleMaintain/toEditArticle.do?edithome=list&noteFileStr=%s'>简辑</a>".formatted(cplArticle.getEncodeNoteFileStr()));
                    }
                }
            }
        }

        if (flyContext.requestConfig().isPc()) {
            builder.append("(").append(DateUtil.getDayStrCn(cplArticle.getModifyTime())).append(")");
        }
        return builder.toString();
    }

    public boolean isTopping() {
        return cplArticle.getArticleOrganize().isTopping();
    }

    public String getNoteFileStr() {
        return cplArticle.getNoteFileStr();
    }


    public String getHrefTitle() {
        return buildHrefTitle(cplArticle);
    }


    public String getArticleOperations() {
        return buildArticleOperations(flyContext, cplArticle);
    }


    public Date getModifyTime() {
        return cplArticle.getModifyTime() != null ? cplArticle.getModifyTime() : cplArticle.getCreateTime();
    }

    public Date getModifyTime4Sort() {
        Date date = cplArticle.getModifyTime() != null ? cplArticle.getModifyTime() : cplArticle.getCreateTime();
        if ("other".equals(cplArticle.getArticleOrganize().getFileType())) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DATE, -100);
            return calendar.getTime();
        } else {
            return date;
        }

    }

    public int getOrderNum() {
        return cplArticle.getArticleOrganize().getOrderNum();
    }


}
