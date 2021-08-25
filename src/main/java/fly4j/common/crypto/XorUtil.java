package fly4j.common.crypto;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;

/**
 * 图片工具
 * Created by qryc on 2016/10/31.
 */
public class XorUtil {


    public static void encryptFile(Path sourceFileUrl, Path targetFileUrl, int pass) {
        FileInputStream in = null;
        FileOutputStream out = null;
        try {
            in = new FileInputStream(sourceFileUrl.toFile());
            out = new FileOutputStream(targetFileUrl.toFile());
            int data = 0;
            while ((data = in.read()) != -1) {
                //将读取到的字节异或上一个数，加密输出
                out.write(data ^ pass);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //在finally中关闭开启的流
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void decryptFile(Path sourceFileUrl, Path targetFileUrl, int pass) {
        FileInputStream in = null;
        FileOutputStream out = null;
        try {
            in = new FileInputStream(sourceFileUrl.toFile());
            out = new FileOutputStream(targetFileUrl.toFile());
            int data = 0;
            while ((data = in.read()) != -1) {
                //将读取到的字节异或上一个数，加密输出
                out.write(data ^ pass);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //在finally中关闭开启的流
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static byte[] decryptFile2Byte(Path sourceFileUrl, int pass) {
        FileInputStream in = null;
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();


            in = new FileInputStream(sourceFileUrl.toFile());
            int data = 0;
            while ((data = in.read()) != -1) {
                //将读取到的字节异或上一个数，加密输出
                out.write(data ^ pass);
            }
            byte[] byteArray = out.toByteArray();
            return byteArray;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //在finally中关闭开启的流
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

}
