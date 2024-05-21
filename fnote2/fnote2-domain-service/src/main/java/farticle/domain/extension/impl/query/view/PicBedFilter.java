package farticle.domain.extension.impl.query.view;

import farticle.domain.extension.query.ArticleViewFilter;
import farticle.domain.view.ArticleView;
import fnote.common.StorePathService;
import fnote.domain.config.FlyConfig;
import fnote.domain.config.FlyContext;

import java.io.File;
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
    private StorePathService pathService;
    private Map<String, String> orignToReplese4Local = new HashMap<>();

    public PicBedFilter() {
//        orignToReplese4Local.put("http://fly4j.cn/", "http://localhost:8080/");
    }

    @Override
    public void filter(ArticleView articleView, FlyContext flyContext) {
        //替换图床
//        Path gitPath = this.getStoreDirPath("git", null).resolve("pic");
//        //已经转移到LoginFilter
////        content = content.replaceAll("/Volumes/HomeWork/doc/pic/", "/viewFile?absolutePath=" + URLEncoder.encode(gitPath.toString(), StandardCharsets.UTF_8) + "/");
//        return content.replaceAll("https://raw.githubusercontent.com/qryc/pic/master/", "/viewFile?absolutePath=" + URLEncoder.encode(gitPath.toString(), StandardCharsets.UTF_8) + "/");
        String content = articleView.getHtml();
        if (!FlyConfig.onLine) {
            for (Map.Entry<String, String> orignToRepleseEntry : orignToReplese4Local.entrySet()) {
                content = content.replaceAll(orignToRepleseEntry.getKey(), orignToRepleseEntry.getValue());
            }
        }
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

    //解决跨域问题
    public String replease4View(String content) {
        Path gitPath = pathService.getUDirPath("git", null).resolve("pic");
        //已经转移到LoginFilter
//        content = content.replaceAll("/Volumes/HomeWork/doc/pic/", "/viewFile?absolutePath=" + URLEncoder.encode(gitPath.toString(), StandardCharsets.UTF_8) + "/");
        return content.replaceAll("https://raw.githubusercontent.com/qryc/pic/master/", "/viewFile?absolutePath=" + URLEncoder.encode(gitPath.toString(), StandardCharsets.UTF_8) + "/");
    }

    //解决编辑跨域问题，兼容一段老逻辑
    public String replease4Edit(String content) {
        Path gitPath = pathService.getUDirPath("git", null).resolve("pic");
        return content.replaceAll("https://raw.githubusercontent.com/qryc/pic/master/", "/Volumes/HomeWork/doc/pic/");
    }

    //filter可以解决编辑器的显示问题，无法解决跨域问题
    public String replease4Filter(String reqURI) {
        String rewriteUrl = replease4FilterInner(reqURI, "/Volumes/HomeWork/doc/pic/");
        return rewriteUrl;
    }

    private String replease4FilterInner(String reqURI, String picPre) {
        if (reqURI.startsWith(picPre)) {
            String absolutePath = pathService.getUDirPath("git", null).resolve("pic").resolve(reqURI.substring(picPre.length())).toString();
            String rewriteUrl = "/viewFile?absolutePath=" + URLEncoder.encode(absolutePath, StandardCharsets.UTF_8);
            return rewriteUrl;
        }
        return null;
    }

    public void setPathService(StorePathService pathService) {
        this.pathService = pathService;
    }
}
