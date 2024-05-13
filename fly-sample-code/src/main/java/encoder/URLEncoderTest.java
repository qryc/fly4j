package encoder;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class URLEncoderTest {
    public static void main(String[] args) {
        System.out.println(java.net.URLEncoder.encode("静夜思/a测试.txt", StandardCharsets.UTF_8));
        System.out.println(URLDecoder.decode("%E9%9D%99%E5%A4%9C%E6%80%9D%2Fa%E6%B5%8B%E8%AF%95.txt",StandardCharsets.UTF_8));
    }
}
