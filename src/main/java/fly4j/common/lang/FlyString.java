package fly4j.common.lang;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * Created by qryc on 2018/12/9.
 */
public class FlyString {
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
        char a=38;
        System.out.println(a);
    }
}
