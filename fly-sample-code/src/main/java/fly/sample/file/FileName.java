package fly.sample.file;

import org.apache.commons.io.FilenameUtils;

public class FileName {
    public static void main(String[] args) {
        String filePath = "/export/图书/中国古典/唐诗/静夜思.md";
        System.out.println(FilenameUtils.getName(filePath));

        filePath = "/export/图书/中国古典/唐诗";
        System.out.println(FilenameUtils.getName(filePath));
    }
}
