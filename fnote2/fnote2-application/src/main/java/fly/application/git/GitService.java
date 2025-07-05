package fly.application.git;

import fly4j.common.util.StringConst;
import fnote.domain.config.FlyConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class GitService {
    static final Logger log = LoggerFactory.getLogger(GitService.class);
    private static List<String> gitDirs = new ArrayList<>();


    static {
        gitDirs.add("/root/export/myBulldozer");
        gitDirs.add("/root/export/MdArticle");
    }


    public static void asynPullAndCommitGit(String source) {
        if (!FlyConfig.onLine) {
            log.info("test environment not permit to exe git asynPullAndCommitGit " + source);
            return;
        }
        new Thread() {
            @Override
            public void run() {
                GitService.pullAndCommitGit(source);
            }
        }.start();

    }

    /**
     * 同步信息
     */
    public static void pullAndCommitGit(String source) {
        if (!FlyConfig.onLine) {
            log.info("test environment not permit to exe git pullAndCommitGit " + source);
            return;
        }
        gitDirs.stream().forEach((dir) -> {
            //拉取最新版本
            pull(source, dir);
            //提交变化
            commitAndPush(source, dir);
        });
    }

    private static void commitAndPush(String source, String dir) {
        log.info(source + " Git:cd %s; git add . ; git commit -am 'init' ; git push".formatted(dir));
        log.info(exeCommand("cd %s; git add . ; git commit -am 'init' ; git push".formatted(dir)));
    }

    private static void pull(String source, String dir) {
        log.info(source + " Git:cd %s; git pull".formatted(dir));
        log.info(exeCommand("cd %s; git pull".formatted(dir)));
    }


    private static String status() throws IOException, InterruptedException {
        log.info("Git:cd /root/FLY; git status -s");
        return exeCommand("cd /root/FLY; git status -s");
    }

    private static String exeCommand(String command) {
        //执行linux命令，不关心返回结果，此处，可以执行一个shell脚本，或者python脚本
//        Process p = r.exec("git status -s");
//        Process p = Runtime.getRuntime().exec(" pwd");
//        Process p = new ProcessBuilder("pwd").start();
        try {
            Process p = new ProcessBuilder("bash", "-c", command).start();
            p.waitFor();
            try (BufferedReader b = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
                String line = "";
                StringBuffer sb = new StringBuffer();
                while ((line = b.readLine()) != null) {
                    sb.append(line).append(StringConst.N_BR);
                }
                return sb.toString();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
