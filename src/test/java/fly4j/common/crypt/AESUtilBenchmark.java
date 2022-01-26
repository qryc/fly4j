package fly4j.common.crypt;

import fly4j.common.crypto.AESUtil;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

/**
 * 2022-01-26
 * @author qryc
 */
public class AESUtilBenchmark {
    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(AESUtilBenchmark.class.getSimpleName())
                .forks(1)
                .build();

        new Runner(opt).run();
    }
    @Benchmark
    public void encryptAndDecrypt() {
        String content = "test中国";
        String password = "12345678";
        // 加密
        byte[] encryptResult = AESUtil.encryptByteToByte(content.getBytes(), password.getBytes());
        // 解密
        byte[] decryptResult = AESUtil.decryptByteToByte(encryptResult, password.getBytes());
        System.out.println("run encryptAndDecrypt");
    }
}
