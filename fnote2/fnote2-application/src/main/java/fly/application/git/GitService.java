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
    private static List<String> gitPullPushDirs = new ArrayList<>();
    private static List<String> gitPullDirs = new ArrayList<>();


    static {
        gitPullPushDirs.add("/root/root/transfer2server");
    }

    public static void main(String[] args) throws Exception {
//        commitAndPush();
//        System.out.println(gitPullPushDirs);

        Stream.concat(gitPullPushDirs.stream(), gitPullDirs.stream()).forEach((dir) -> {
            System.out.println(dir);
        });
    }

    public static String exe(String type, String source) {
        try {
            return switch (type) {
                case "status" -> status();
                case "pull" -> pull(source);
                case "pullAll" -> pullAll(source);
                case "commitAndPush" -> commitAndPush(source);
                default -> "notSurport";
            };
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public static void asynPullAndCommitGit(String source) {
        if (FlyConfig.onLine) {
            new Thread() {
                @Override
                public void run() {
                    GitService.pullAndCommitGit(source);
                }
            }.start();

        }
    }

    /**
     * 同步信息
     */
    public static String pullAndCommitGit(String source) {
        final StringBuilder strBuilder = new StringBuilder();

        //拉取最新版本
        Stream.concat(gitPullPushDirs.stream(), gitPullDirs.stream()).forEach((dir) -> {
            log.info(source + " Git:cd %s; git pull".formatted(dir));
            strBuilder.append(exeCommand("cd %s; git pull".formatted(dir)));
        });
        //提交变化
        gitPullPushDirs.forEach((dir) -> {
            log.info(source + " Git:cd %s; git add . ; git commit -am 'init' ; git push".formatted(dir));
            strBuilder.append(exeCommand("cd %s; git add . ; git commit -am 'init' ; git push".formatted(dir)));
        });
        return strBuilder.toString();
    }

    private static String pull(String source) throws IOException, InterruptedException {
        final StringBuilder strBuilder = new StringBuilder();

        gitPullPushDirs.forEach((dir) -> {
            log.info(source + " Git:cd %s; git pull".formatted(dir));
            strBuilder.append(exeCommand("cd %s; git pull".formatted(dir)));
        });
        return strBuilder.toString();
    }

    /**
     * 拉取所有
     */
    public static String pullAll(String source) throws IOException, InterruptedException {
        log.info("Git:cd all" + source);
        final StringBuilder strBuilder = new StringBuilder();
        Stream.concat(gitPullPushDirs.stream(), gitPullDirs.stream()).forEach((dir) -> {
            log.info(source + ":Git:cd %s; git pull".formatted(dir));
            if (FlyConfig.onLine) {
                strBuilder.append(exeCommand("cd %s; git pull".formatted(dir)));
            } else {

            }
        });

        return strBuilder.toString();

    }

    private static String commitAndPush(String source) throws IOException, InterruptedException {
        log.info(source+" Git:cd /root/FLY; git add . ; git commit -am 'init' ; git push");
        return exeCommand("cd /root/FLY; git add . ; git commit -am 'init' ; git push");
    }

    private static String status() throws IOException, InterruptedException {
        log.info("Git:cd /root/FLY; git status -s");
        return exeCommand("cd /root/FLY; git status -s");
    }

    private static String push(String source) throws IOException, InterruptedException {
        log.info(source+" Git:cd /root/FLY; git push");
        return exeCommand("cd /root/FLY; git push");
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
