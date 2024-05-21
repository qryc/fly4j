package fnote.deploy.controller;

import fly4j.common.util.DateUtil;
import fly4j.common.util.RandomUtil;
import org.apache.commons.lang.text.StrBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;

public class FullFile {
    public static void main(String[] args) throws Exception {

//        tenM();
        for (int i = 0; i < 30; i++) {
            final int threadid = i;
            Thread.sleep(1000);
            new Thread() {
                @Override
                public void run() {
                    System.out.println("s"+threadid);
                    try {
                        oneM(threadid, 10);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }.start();

        }
    }

    private static void oneM(int threadid, int m) throws IOException {
        long fileSize = Integer.MAX_VALUE;
        for (long i = 0; i < fileSize; i++) {
            Path path = Path.of("/Users/gw/NotBack/FullFile/%s/t%s".formatted(DateUtil.getDayStr(new Date()), threadid), "fwerwerw" + i);
            if (Files.notExists(path.getParent()))
                Files.createDirectories(path.getParent());
            StrBuilder strBuilder = new StrBuilder();
            for (int j = 0; j < 1024; j++) {
                String pwd = RandomUtil.random(1024 * m, true, true);
                strBuilder.append(pwd);
            }
            Files.writeString(path, strBuilder.toString());
            System.out.printf("thread%s:%s%n", threadid, i);

        }
    }
}
