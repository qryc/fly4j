package fly.english;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * 多个单词文件去重
 */
public class VocabularyAnalysis {
    public static void main(String[] args) throws IOException {

        String baseDir = "/Volumes/HomeWork/transfer2server/MdArticle/z4 英语学习/vocabulary/";
        String baseDirTemp = "/Volumes/HomeWork/temp/";
        List<String> fileNames = List.of(
                baseDir + "myKnow.txt"
                ,baseDir + "myCOCA1.txt"
                , baseDir + "myCOCA2.txt"
                , baseDir + "myCOCA3.txt"
                , baseDir + "newAdd.txt"
        );
        Map<String, String> vocabularyMap = new LinkedHashMap<>();

        FileUtils.deleteDirectory(new File(baseDirTemp + "result"));


        for (String fileName : fileNames) {
            List<String> tempVocabularys = FileUtils.readLines(new File(fileName));
            List<String> result = new ArrayList<>();//输出去重后的结果
            for (int i = 0; i < tempVocabularys.size(); i++) {
                String vocabulary = tempVocabularys.get(i);
                if (vocabularyMap.containsKey(vocabulary)) {
                    System.out.println(vocabulary + " in file:" + fileName + "-" + (i + 1) + " already in file:" + vocabularyMap.get(vocabulary));
                } else {
                    vocabularyMap.put(vocabulary, fileName + "-" + (i + 1));
                    result.add(vocabulary);
                }
            }

            if (tempVocabularys.size() != result.size()) {
                System.out.println("repeat：%d to %d in file %s".formatted(tempVocabularys.size(), result.size(), fileName));
                File resultFile = new File(baseDirTemp + "result/result" + fileName.split("/")[fileName.split("/").length - 1] + ".txt");
                FileUtils.writeLines(resultFile, result);
            } else {
                System.out.println("no repeat in file %s".formatted(fileName));

            }

        }
        System.out.println("all vocabulary:" + vocabularyMap.size());
    }
}
