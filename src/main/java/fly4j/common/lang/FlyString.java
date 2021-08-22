package fly4j.common.lang;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by qryc on 2018/12/9.
 */
public class FlyString {
    public static Map<String, String> compare2Str(List<String> list) {
        int sameIndex = getSameIndex(list);

        Map<String, String> map = new HashMap<>();
        map.put("same", list.get(0).substring(0, sameIndex + 1));
        for (int i = 0; i < list.size(); i++) {
            map.put("" + i, list.get(i).substring(sameIndex + 1));
        }

        return map;
    }

    public static int getSameIndex(List<String> list) {
        int sameIndex = 0;
        int length = list.get(0).length();
        for (String s : list) {
            if (s.length() < length) {
                length = s.length();
            }
        }
        for (int i = 0; i < length; i++) {
            boolean same = true;
            for (String s : list) {

                if (list.get(0).substring(0, i + 1).equals(s.substring(0, i + 1))) {

                } else {
                    same = false;
                    break;
                }
            }
            if (same) {
                sameIndex = i;
            } else {
                break;
            }
        }


        return sameIndex;
    }

    public static String getPlanText(String str) {
        /**
         * [任意]
         * ^不是
         * +多次，正则表达式9+匹配9、99、999等
         */
        return str.replaceAll("[^0-9a-zA-Z\u4e00-\u9fa5-_]+", "");
    }

    /**
     * 求一个字符串的md5值
     *
     * @param target 字符串
     * @return md5 value
     */
    private String MD5(String target) {
        return DigestUtils.md5Hex(target);
    }

    public static String htmEncode(String s) {
        StringBuffer stringbuffer = new StringBuffer();
        int j = s.length();
        for (int i = 0; i < j; i++) {
            char c = s.charAt(i);
            switch (c) {
                case 60:
                    stringbuffer.append("&lt;");
                    break;
                case 62:
                    stringbuffer.append("&gt;");
                    break;
                case 38:
                    stringbuffer.append("&amp;");
                    break;
//			case 34:
//				stringbuffer.append("&quot;");
//				break;
//			case 169:
//				stringbuffer.append("&copy;");
//				break;
//			case 174:
//				stringbuffer.append("&reg;");
//				break;
//			case 165:
//				stringbuffer.append("&yen;");
//				break;
//			case 8364:
//				stringbuffer.append("&euro;");
//				break;
//			case 8482:
//				stringbuffer.append("&#153;");
//				break;
//			case 13:
//				if (i < j - 1 && s.charAt(i + 1) == 10) {
//					stringbuffer.append("<br>");
//					i++;
//				}
//				break;
//			case 32:
//				if (i < j - 1 && s.charAt(i + 1) == ' ') {
//					stringbuffer.append("&nbsp;");
//					i++;
//					break;
//				}
                default:
                    stringbuffer.append(c);
                    break;
            }
        }
        return stringbuffer.toString();
    }

    public static void main(String[] args) {
        System.out.println(FlyString.getPlanText("a  大&.，,。？“”…0…%￥*&）..小"));
        System.out.println(FlyString.htmEncode("<a>sdf</a>fw"));
        char a = 38;
        System.out.println(a);
    }
}
