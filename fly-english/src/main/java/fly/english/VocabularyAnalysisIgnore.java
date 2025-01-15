package fly.english;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 多个单词文件去重,忽略大小写
 */
public class VocabularyAnalysisIgnore {
    public static void main(String[] args) throws IOException {

        String baseDir = "/Volumes/HomeWork/transfer2server/MdArticle/z4 英语学习/vocabulary/";
        String baseDirTemp = "/Volumes/HomeWork/temp/";
        List<String> fileNames = List.of(
                baseDir + "myCOCA1.txt"
                , baseDir + "myCOCA2.txt"
                , baseDir + "myCOCA3.txt");
        Map<String, String> vocabularyMap = new LinkedHashMap<>();

        FileUtils.deleteDirectory(new File(baseDirTemp + "result"));


        for (String fileName : fileNames) {
            List<String> tempVocabularys = FileUtils.readLines(new File(fileName));
            for (int i = 0; i < tempVocabularys.size(); i++) {
                String vocabulary = tempVocabularys.get(i);
                if (vocabularyMap.containsKey(vocabulary.toLowerCase())) {
                    System.out.println(vocabulary + " in file:" + fileName + "-" + (i + 1) + " already in file:" + vocabularyMap.get(vocabulary.toLowerCase()));
                } else {
                    vocabularyMap.put(vocabulary.toLowerCase(), fileName + "-" + (i + 1));
                }
            }



        }
        System.out.println("all vocabulary:" + vocabularyMap.size());
    }
}
