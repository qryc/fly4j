package fly.sample.file;

import org.apache.commons.io.FilenameUtils;

public class FilePathName {
    public static void main(String[] args) {
        String macFilePath = "/export/图书/中国古典/唐诗/静夜思.md";
        String fileAbsolutePath = FilenameUtils.separatorsToUnix(macFilePath);
        System.out.println(fileAbsolutePath);
        String windowFilePath = "d:\\图书\\中国古典\\唐诗\\静夜思.md";
        fileAbsolutePath = FilenameUtils.separatorsToUnix(windowFilePath);
        System.out.println(fileAbsolutePath);



        System.out.println(System.getProperty("user.home"));
        System.out.println(System.getProperty("user.dir"));

    }
}
