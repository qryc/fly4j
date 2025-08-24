package fnote.common.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlUtil {
    private static String srcInImgHtmlRegex = "<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>";

    public static List<String> getSrcFromImagTag(String html) {
        List<String> srcList = new ArrayList<>();
        Pattern imgPattern = Pattern.compile(srcInImgHtmlRegex);
        Matcher matcher = imgPattern.matcher(html);
        while (matcher.find()) {
            srcList.add(matcher.group(1));
        }
        return srcList;
    }

}
