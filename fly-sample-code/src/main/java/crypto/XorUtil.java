package crypto;

import org.apache.commons.codec.CharEncoding;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * 图片工具
 * Created by qryc on 2016/10/31.
 */
public class XorUtil {
    public static String encryptStr2HexStr(String content, int password) {
        try {
            byte[] byteContent = content.getBytes(CharEncoding.UTF_8);
            byte[] encryptBytes = xorByte2Byte(byteContent, password);

            //把字节码转换为16进制字符
            return ByteUtil.parseByte2HexStr(encryptBytes);
        } catch (Exception e) {
            throw new RuntimeException(e);

        }

    }
    public static String decryptHexStrToStr(String hexEncryptStr,int password) {
        try {
            byte[] encryptBytes = ByteUtil.parseHexStr2Byte(hexEncryptStr);
            byte[] byteContent = xorByte2Byte(encryptBytes, password);
            return new String(byteContent, CharEncoding.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("decryptHexStrToStr", e);
        }
    }
    private static void xorEncrypt(InputStream in, OutputStream out, int pass) throws IOException {
        int data = 0;
        while ((data = in.read()) != -1) {
            //将读取到的字节异或上一个数，加密输出
            out.write(data ^ pass);
        }
    }


    public static byte[] xorByte2Byte(byte[] bytes, int pass) throws IOException {
        try (InputStream in = new ByteArrayInputStream(bytes); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            xorEncrypt(in, out, pass);
            return out.toByteArray();
        }
    }
}
