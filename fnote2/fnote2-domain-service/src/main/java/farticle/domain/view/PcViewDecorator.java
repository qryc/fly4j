package farticle.domain.view;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by qryc on 2019/12/14.
 */
public class PcViewDecorator {
    private static final String imgDefaultWidth = "90%";
    // 获取img标签正则 .表示任何字符 *零次或多次 ?一次或多次 ( [限定] ;
    private static final String IMGURL_REG = "<img[^<>]*>";
    private static final String TABLE_REG = "<table[^<>]*>";
    private static final String WIDTH_REG = "width=[\\S]*";
    private static final String SRC_REG = "src=[\\S]*";
    private static final String HEIGHT_REG = "height=[\\S]*";
    private static final String CSS_WIDTH_REG = "width[^<>]*;";
    private static final String CSS_HEIGHT_REG = "height=[^<>]*;";


    private static List<String> getPartenStrings(String parten, String html) {
        Matcher matcher = Pattern.compile(parten).matcher(html);
        List<String> listImgUrl = new ArrayList<String>();
        while (matcher.find()) {
            listImgUrl.add(matcher.group());
        }
        return listImgUrl;
    }

    private static List<String> getImgWidthByImgHtml(String html) {
        Matcher matcher = Pattern.compile(WIDTH_REG).matcher(html);
        List<String> listImgUrl = new ArrayList<String>();
        while (matcher.find()) {
            listImgUrl.add(matcher.group());
        }
        return listImgUrl;
    }

    public static void main(String[] args) {
//        String testHtml = "<table style=\"width: 721px; height: 213px;\" border=\"1\" cellspacing=\"0\" cellpadding=\"0\">" +
//                "一。背景<br />&nbsp; &nbsp; &nbsp;原来的用户登录cookies，只依赖于客户端的状态，服务器端无记录。这样如果客户端的cookies正确，就没有办法踢出用户。<br />二。设计<br />集中式session和其它系统关系<br /><img src=\"../pic/335/407.jpg\" alt=\" \" width=\"486\" height=\"386\" /><br />三。水平扩展方案(简单利用从)<br />主从复制走内网，登录走外网，在内网同步比外网跳转快的时候适用。缺点是如果内网慢会踢出登录，不适用。<br /><img src=\"../pic/335/520.jpg\" alt=\" \" width=\"465\" height=\"258\" /><br /><br />四。水平扩展方案(按时间利用主从)<br />没有上一个方案的限制，根据时间来选择走主，还是走从。缺点：时间如果设置过长，从库利用不够充分。<br /><img src=\"../pic/335/521.jpg\" alt=\" \" width=\"461\" height=\"273\" /><br />五。水平扩展方案(回源主)<br />先使用从redis，如果从redis没有数据，并且创建时间很短，回源主redis。从库得到最大利用。缺点：如果异常数据过多会给主库造成压力。但集中式session传来的数据已经过了一层检验，异常数据很少。此方案还可以监控有多少数据延迟到了影响使用的程度。<br />需要考虑如果客户端加密破解的情况，这时候会伪造出很多不存在的，如果回源就会给主redis带来压力。<br />但如上几个方案都解决不了这个问题。此方案到时候还可以选择关掉回源，还是比上个方案好。<br /><br /><img src=\"../pic/335/519.jpg\" alt=\" \" width=\"542\" height=\"312\" /><br /><br /><br />五。多中心方案(打标分流写)<br /><img src=\"../pic/335/522.jpg\" alt=\" \" width=\"380\" height=\"400\" /><br />只有在应用的前端做好了路由，才可以不跨机房调用，效率才会好，如果在应用层做pin路由，就会产生跨机房调用，如下图：<br /><img src=\"../pic/335/524.jpg\" alt=\" \" width=\"269\" height=\"282\" /><br />六。redis结构优化<br /><img src=\"../pic/335/518.jpg\" alt=\" \" width=\"512\" height=\"303\" /><br />redis结构优化<br /><img src=\"../pic/335/537.jpg\" alt=\" \" width=\"454\" height=\"354\" /><br /><br />\n" +
//                "";
//        String newHtml = optimizView(testHtml,true);
//        System.out.println(testHtml);
//        System.out.println(newHtml);
        System.out.println("<p><img src=\" /pic/iknowDisk/设计/数据迁徙/数据迁徙-切量.jpg\" width=\"800\"  />".replaceAll("\"", ""));
    }

    public static String optimizView(String htmlContent) {
        if (StringUtils.isBlank(htmlContent)) {
            return htmlContent;
        }
        //匹配出所有的Img标签
        List<String> listImgLabel = getPartenStrings(IMGURL_REG, htmlContent);
        for (String imgStrOld : listImgLabel) {


            List<String> listWidthUrl = getPartenStrings(WIDTH_REG, imgStrOld);
            if (CollectionUtils.isEmpty(listWidthUrl)) {
                //替换所有的宽度为90%
                String imgStrNew = "";
                if (imgStrOld.contains("/>")) {
                    imgStrNew = imgStrOld.replaceAll("/>", "width=\"" + imgDefaultWidth + "\" />");
                } else {
                    imgStrNew = imgStrOld.replaceAll(">", "width=\"" + imgDefaultWidth + "\" >");
                }
                //消除所有的高度
                imgStrNew = imgStrNew.replaceAll(HEIGHT_REG, "");
                htmlContent = htmlContent.replace(imgStrOld, imgStrNew);
            } else {
                //如果设置了width并且大于800，调整为800
                String widthStr = listWidthUrl.get(0);
                if (!widthStr.contains("%")) {
                    int width = Integer.valueOf(widthStr.replaceAll("width=", "").replaceAll("'", "").replaceAll("\"", ""));
                    if (width > 800) {
                        //替换所有的宽度为100%
                        String imgStrNew = imgStrOld.replaceAll(WIDTH_REG, "width=\"" + imgDefaultWidth + "\"");
                        //消除所有的高度
                        imgStrNew = imgStrNew.replaceAll(HEIGHT_REG, "");
                        htmlContent = htmlContent.replace(imgStrOld, imgStrNew);
                    }
                }


            }
        }
//        newHtml = resetTableSize(testHtml);

        return htmlContent;
    }


    public static String resetTableSize(String testHtml) {
        List<String> listImgUrl = getPartenStrings(TABLE_REG, testHtml);
        for (String imgStr : listImgUrl) {
            String imgStrNew = imgStr.replaceAll(CSS_WIDTH_REG, "width: 100%;");
            imgStrNew = imgStrNew.replaceAll(CSS_HEIGHT_REG, "");
            testHtml = testHtml.replaceAll(imgStr, imgStrNew);
        }
        return testHtml;
    }

}
