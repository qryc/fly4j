package farticle.domain.extension.impl.query.view;

import farticle.domain.extension.query.ArticleViewFilter;
import farticle.domain.view.ArticleView;
import fnote.common.StorePathService;
import fnote.domain.config.FlyConfig;
import fnote.domain.config.FlyContext;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PicBedFilter implements ArticleViewFilter {
    private StorePathService pathService;
    private Map<String, String> orignToReplese4Local = new HashMap<>();
    private boolean replace2Local = false;

    public PicBedFilter() {
        orignToReplese4Local.put("http://fly4j.cn/", "http://localhost:8080/");
    }

    @Override
    public void filter(ArticleView articleView, FlyContext flyContext) {
        String content = articleView.getHtml();

        //替换本地图片
        if (!FlyConfig.onLine && replace2Local) {
            for (Map.Entry<String, String> orignToRepleseEntry : orignToReplese4Local.entrySet()) {
                content = content.replaceAll(orignToRepleseEntry.getKey(), orignToRepleseEntry.getValue());
            }
        }

        //替换为绝对路径
        String pattern = "<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>";
        Pattern imgPattern = Pattern.compile(pattern);
        Matcher matcher = imgPattern.matcher(articleView.getHtml());
        while (matcher.find()) {
            String src = matcher.group(1);
//            System.out.println("src: " + URLDecoder.decode(src, StandardCharsets.UTF_8));
            //直接替换博客地址为绝对路径
            Path srcPath = Path.of(articleView.getCplArticle().getNoteFileStr()).getParent().resolve(URLDecoder.decode(src, StandardCharsets.UTF_8));
            if (Files.exists(srcPath)) {
//                content = content.replaceAll(src, URLEncoder.encode(srcPath.toString(), StandardCharsets.UTF_8));
                content = content.replaceAll(src, srcPath.toString());

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


    public void setPathService(StorePathService pathService) {
        this.pathService = pathService;
    }
}
