package fly.english;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class VocabularySingle {
    public static void main(String[] args) throws IOException {
        String baseDir = "/Volumes/HomeWork/transfer2server/MdArticle/z4 英语学习/";
        String fileName = baseDir + "myCOCA3.txt";
        Map<String, String> vocabularyMap = new LinkedHashMap<>();
        List<String> tempVocabularys = FileUtils.readLines(new File(fileName));
        List<String> result = new ArrayList<>();
        for (int i = 0; i < tempVocabularys.size(); i++) {
            String vocabulary = tempVocabularys.get(i);
            if (vocabularyMap.containsKey(vocabulary)) {
                System.out.println(vocabulary + " in file:" + fileName + "-" + (i + 1) + " already in file:" + vocabularyMap.get(vocabulary));
            } else {
                vocabularyMap.put(vocabulary, fileName + "-" + (i + 1));
                result.add(vocabulary);
            }
        }

        System.out.println("all vocabulary:" + vocabularyMap.size());
//        vocabularyMap.forEach((k, v) -> System.out.println(k)
//        );

        File resultFile = new File(baseDir + "result.txt");
        FileUtils.deleteQuietly(resultFile);
        FileUtils.writeLines(resultFile, result);
    }
}
