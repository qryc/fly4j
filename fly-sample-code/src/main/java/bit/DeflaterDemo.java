package bit;

import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class DeflaterDemo {
    public static void main(String[] args) throws Exception {
        System.out.println("中国 字节长度：%s".formatted(("中国").getBytes("UTF-8").length));
        System.out.println("ok 字节长度：%s".formatted(("ok").getBytes("UTF-8").length));
        String message = "hello word:".repeat(10);
        System.out.println("原始的字符串长度 : " + message.length());
        byte[] input = message.getBytes("UTF-8");
        System.out.println("原始的字节长度 : " + input.length);

        // 压缩
        byte[] output = new byte[1024];
        Deflater deflater = new Deflater();
        deflater.setInput(input);
        deflater.finish();
        int compressedDataLength = deflater.deflate(output);
        deflater.end();

        System.out.println("压缩后的字节长度：" + compressedDataLength + " 字节:" + output);

        //解压缩
        Inflater inflater = new Inflater();
        inflater.setInput(output, 0, compressedDataLength);
        byte[] result = new byte[1024];
        int resultLength = inflater.inflate(result);
        inflater.end();

        // Decode the bytes into a String
        message = new String(result, 0, resultLength, "UTF-8");

        System.out.println("UnCompressed Message length : " + message.length());
    }
}
