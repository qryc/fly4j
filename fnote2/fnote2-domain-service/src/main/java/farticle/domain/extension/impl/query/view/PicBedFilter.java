package farticle.domain.extension.impl.query.view;

import farticle.domain.extension.query.ArticleViewFilter;
import farticle.domain.view.ArticleView;
import fnote.common.PathService;
import fnote.common.web.HtmlUtil;
import fnote.domain.config.FlyConfig;
import fnote.domain.config.FlyContext;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PicBedFilter implements ArticleViewFilter {
    private PathService pathService;
    private boolean replace2Local = false;
    private Map<String, String> orignToReplese4Local = new HashMap<>();


    public PicBedFilter() {
        orignToReplese4Local.put("http://fly4j.cn/", "http://localhost:8080/");
    }

    @Override
    public void filter(ArticleView articleView, FlyContext flyContext) {
        String content = articleView.getHtml();

        //replace local picture host
        if (!FlyConfig.onLine && replace2Local) {
            for (Map.Entry<String, String> orignToRepleseEntry : orignToReplese4Local.entrySet()) {
                content = content.replaceAll(orignToRepleseEntry.getKey(), orignToRepleseEntry.getValue());
            }
        }

        //replace absolute img path
        List<String> srcList = HtmlUtil.getSrcFromImagTag(articleView.getHtml());
        for (String src : srcList) {
            //absolute src path
            Path srcPath = Path.of(articleView.getCplArticle().getNoteFileStr()).getParent().resolve(URLDecoder.decode(src, StandardCharsets.UTF_8));
            if (Files.exists(srcPath)) {
                //replace src with absolute src
                content = content.replaceAll(src, URLEncoder.encode(srcPath.toString(), StandardCharsets.UTF_8));
            }
        }
        articleView.setHtml(content);
    }


    public void setPathService(PathService pathService) {
        this.pathService = pathService;
    }
}
