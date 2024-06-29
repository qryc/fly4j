package farticle.domain.extension.impl.query.view;

import farticle.domain.extension.query.ArticleViewFilter;
import farticle.domain.view.ArticleView;
import farticle.domain.view.CommonMdDecorator;
import farticle.domain.view.TreeService;
import fnote.domain.config.FlyContext;
import org.apache.commons.lang3.StringUtils;

public class MdFilter implements ArticleViewFilter {
    @Override
    public void filter(ArticleView articleView, FlyContext flyContext) {
        if (articleView.getCplArticle().getArticleOrganize().isMdType()) {
            articleView.setHtml(covertOriginal2Html(articleView.getHtml(), flyContext));
        }

    }

    public String covertOriginal2Html(String originalText, FlyContext flyContext) {
        //长度小于1000,不展示导航树
        if (originalText.length() < 1000) {
            return CommonMdDecorator.parse(originalText);
        }
        if (flyContext != null) {
            if (!flyContext.clientConfig().isFullScreen()) {
//                String pageTreeStr = TreeService.getPageTreeStr(originalText);
                originalText = repleaseAnchor(originalText);
//                return "目录<br/>" + pageTreeStr + CommonMdDecorator.parse(originalText);
                return CommonMdDecorator.parse(originalText);

            }
        }

        return CommonMdDecorator.parse(originalText);
    }

    public static String repleaseAnchor(String mdStr) {
        var stringBuilder = new StringBuilder();
        var id = 1;
        for (String lineStr : mdStr.split(StringUtils.LF)) {
            //替换为href
            if (lineStr.startsWith("###### ")) {

            } else if (lineStr.startsWith("##### ")) {


            } else if (lineStr.startsWith("#### ")) {


            } else if (lineStr.startsWith("### ")) {
                stringBuilder.append("<h3 id='section-").append(id).append("'>").append(lineStr.replaceAll("###", "").trim()).append("</h3>").append(StringUtils.LF).append(StringUtils.LF);
                id++;
                continue;
            } else if (lineStr.startsWith("## ")) {
                stringBuilder.append("<h2 id='section-").append(id).append("'>").append(lineStr.replaceAll("##", "").trim()).append("</h2>").append(StringUtils.LF).append(StringUtils.LF);
                id++;
                continue;
            } else if (lineStr.startsWith("# ")) {
                stringBuilder.append("<h2 id='section-").append(id).append("'>").append(lineStr.replaceAll("#", "").trim()).append("</h2>").append(StringUtils.LF).append(StringUtils.LF);
                id++;
                continue;

            }
            //原文拼接
            stringBuilder.append(lineStr).append(StringUtils.LF);
        }
        return stringBuilder.toString();
    }

}
