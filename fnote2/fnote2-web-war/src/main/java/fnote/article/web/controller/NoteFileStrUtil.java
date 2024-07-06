package fnote.article.web.controller;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class NoteFileStrUtil {
    public static String decode(String noteFileStrEncode) {
        return URLDecoder.decode(noteFileStrEncode, StandardCharsets.UTF_8);
    }

    public static String encode(String noteFileStr) {
        return URLEncoder.encode(noteFileStr, StandardCharsets.UTF_8);
    }

    public static void main(String[] args) {

    }

}
