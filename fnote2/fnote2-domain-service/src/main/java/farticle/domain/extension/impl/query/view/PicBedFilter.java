package farticle.domain.extension.impl.query.view;

import farticle.domain.extension.query.ArticleViewFilter;
import farticle.domain.view.ArticleView;
import fnote.common.PathService;
import fnote.domain.config.FlyConfig;
import fnote.domain.config.FlyContext;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PicBedFilter implements ArticleViewFilter {
    private PathService pathService;
    private Map<String, String> orignToReplese4Local = new HashMap<>();
    private boolean replace2Local = false;
    private String srcInImgHtmlRegex="<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>";

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
        Pattern imgPattern = Pattern.compile(srcInImgHtmlRegex);
        Matcher matcher = imgPattern.matcher(articleView.getHtml());
        while (matcher.find()) {
            //match find src
            String src = matcher.group(1);
            //absolute src path
            Path srcPath = Path.of(articleView.getCplArticle().getNoteFileStr()).getParent().resolve(URLDecoder.decode(src, StandardCharsets.UTF_8));
            if (Files.exists(srcPath)) {
                //replace src with absolute src
                content = content.replaceAll(src, URLEncoder.encode(srcPath.toString(),StandardCharsets.UTF_8));
            }
        }
        articleView.setHtml(content);
    }

    public static void main(String[] args) {
        String html = "<img src='example1.jpg'> <img src=\"example2.jpg\"> <img src=\"example3.jpg\">";
        String pattern = "<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>";

        Pattern imgPattern = Pattern.compile(pattern);
        Matcher matcher = imgPattern.matcher(html);

        while (matcher.find()) {
            String src = matcher.group(1);
            System.out.println("src: " + src);
        }
    }


    public void setPathService(PathService pathService) {
        this.pathService = pathService;
    }
}
