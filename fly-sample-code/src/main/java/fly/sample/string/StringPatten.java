package fly.sample.string;

import java.util.regex.Matcher;

public class StringPatten {
    public static void main(String[] args) {
        //true
        System.out.println("redis升级/._升级Redis版本方案.docx".contains("._"));
        //redis升级/*升级Redis版本方案.docx
        System.out.println("redis升级/._升级Redis版本方案.docx".replaceAll("._","*"));
        //输出true，需要转义
        System.out.println("d:\\Document".contains("\\"));
        //输出d:/Document，需要双倍转义
        System.out.println("d:\\Document".replaceAll("\\\\", "/"));
        //Illegal/unsupported escape sequence
//        System.out.println(windowFilePath.replaceAll("\\", "/"));
        //输出true
        System.out.println("id=$id".contains("$id"));
        //输出id=$id
        System.out.println("id=$id".replaceAll("$id","4"));
        //输出id=4
        System.out.println("id=$id".replaceAll("\\$id","4"));

        //输出d:/Document
        System.out.println("d:\\Document".replaceAll(Matcher.quoteReplacement("\\"),"/"));
        //输出id=4
        System.out.println("id=$id".replaceAll(Matcher.quoteReplacement("$id"),"4"));

        System.out.println("a*a".replaceAll("\\*", "_"));

    }
}
