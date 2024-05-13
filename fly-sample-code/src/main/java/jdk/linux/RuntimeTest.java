package jdk.linux;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class RuntimeTest {
    public static void main(String[] args) throws Exception {
        commitAndPush();

    }
    private static void pull() throws IOException, InterruptedException {
        exeCommand("cd ..; git pull");
    }
    private static void commitAndPush() throws IOException, InterruptedException {
        exeCommand("cd ..; git status -s");
        exeCommand("cd ..; git add . ; git commit -am 'init' ; git push");
        exeCommand("cd ..; git status -s");
    }
    private static void push() throws IOException, InterruptedException {
//        exeCommand("cd ..; git status -s");
        exeCommand("cd ..; git push");
//        exeCommand("cd ..; git status -s");
    }
    private static void exeCommand(String command) throws IOException, InterruptedException {
        //执行linux命令，不关心返回结果，此处，可以执行一个shell脚本，或者python脚本
//        Process p = r.exec("git status -s");
//        Process p = Runtime.getRuntime().exec(" pwd");
//        Process p = new ProcessBuilder("pwd").start();
        Process p = new ProcessBuilder("bash", "-c", command).start();
        p.waitFor();
        try (BufferedReader b = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
            String line = "";
            StringBuffer sb = new StringBuffer();
            while ((line = b.readLine()) != null) {
                sb.append(line).append("\n");
            }
            System.out.println("result： " + sb);
        }
    }
}
