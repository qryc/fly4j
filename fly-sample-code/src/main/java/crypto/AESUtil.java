package crypto;

import org.apache.commons.codec.CharEncoding;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;

/**
 * @author qryc
 */
final public class AESUtil {
    private AESUtil() {

    }


    public static byte[] encryptByteToByte(byte[] byteContent, byte[] bytePass) {
        byte[] result = new byte[0];
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            // 需手动指定 SecureRandom 随机数生成规则
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            random.setSeed(bytePass);
            kgen.init(128, random);

            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");

            Cipher cipher = Cipher.getInstance("AES");// 创建密码器
            cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化

            result = cipher.doFinal(byteContent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result; // 加密
    }

    /**
     * 解密
     *
     * @param content  待解密内容
     * @param bytePass 解密密钥
     * @return
     */
    public static byte[] decryptByteToByte(byte[] content, byte[] bytePass) {
        // 加密结果改为16进制
        String encryptResultStr = ByteUtil.parseByte2HexStr(content);

        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            random.setSeed(bytePass);
            kgen.init(128, random);

            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");

            Cipher cipher = Cipher.getInstance("AES");// 创建密码器
            cipher.init(Cipher.DECRYPT_MODE, key);// 初始化

            byte[] result = cipher.doFinal(content);
            return result; // 加密
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 根据指定的key使用AES算法加密字节数组
     * // 加密结果改为16进制
     * 第一步：字符串转byte
     * 第二步：加密byte
     * 第三步：加密后的byte转16进制存储
     */
    public static String encryptStr2HexStr(String content, String password) {
        try {
            byte[] byteContent = content.getBytes(CharEncoding.UTF_8);
            byte[] bytePass = password.getBytes(CharEncoding.UTF_8);
            byte[] encryptBytes = encryptByteToByte(byteContent, bytePass);

            //把字节码转换为16进制字符
            return ByteUtil.parseByte2HexStr(encryptBytes);
        } catch (Exception e) {
            throw new RuntimeException(e);

        }

    }

    /**
     * 解密数据
     * 第一步：先把16进制转2进制
     * 第二步：解密2进制
     * 第三步：解密结果转string
     * 加密结果改为16进制
     *
     * @param hexEncryptStr 要解密的16进制数据字符串
     */
    public static String decryptHexStrToStr(String hexEncryptStr, String password) {
        try {
            byte[] bytePass = password.getBytes(CharEncoding.UTF_8);
            byte[] encryptBytes = ByteUtil.parseHexStr2Byte(hexEncryptStr);
            byte[] byteContent = decryptByteToByte(encryptBytes, bytePass);
            return new String(byteContent, CharEncoding.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("decryptHexStrToStr", e);
        }
    }

    public static void main(String[] args) throws Exception {
        String content = "对酒当歌，人生几何！譬如朝露，去日苦多。";
        String password = "mi12456";
        // 加密
        System.out.println("加密前%d：".formatted(content.length()) + content);
        String encryptResult = AESUtil.encryptStr2HexStr(content, password);
        System.out.println("加密后%d：".formatted(encryptResult.length()) + encryptResult);
        // 解密
        String decryptResult = AESUtil.decryptHexStrToStr(encryptResult, password);
        System.out.println("解密后：" + decryptResult);

        // 加密
        encryptResult = XorUtil.encryptStr2HexStr(content, 123456);
        System.out.println("加密后%d：".formatted(encryptResult.length()) + encryptResult);
        // 解密
        decryptResult = XorUtil.decryptHexStrToStr(encryptResult, 123456);
        System.out.println("解密后：" + decryptResult);

        //二进制
        String temp = Hex.encodeHexString(content.getBytes(CharEncoding.UTF_8));
        System.out.println("十六进制%d：".formatted(temp.length()) + temp);
        temp= new String(Hex.decodeHex(temp), CharEncoding.UTF_8);
        System.out.println("解密后：" + temp);

        temp = Base64.encodeBase64String(content.getBytes(CharEncoding.UTF_8));
        System.out.println("64进制%d：".formatted(temp.length()) + temp);
        temp= new String(Base64.decodeBase64(temp), CharEncoding.UTF_8);
        System.out.println("解密后：" + temp);

    }
}
