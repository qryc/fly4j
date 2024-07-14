package farticle.domain.entity;

import java.io.File;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class DtreeComparator implements Comparator<File> {
    @Override
    public int compare(File o1, File o2) {
        if (o1.isDirectory() && o2.isFile())
            return 1;
        if (o1.isFile() && o2.isDirectory())
            return -1;
        int s1 = isSpe(o1.getName());
        int s2 = isSpe(o2.getName());
        if (s1 != -1 && s2 != -1) {
            return s1 - s2;
        }
        return o1.getName().compareTo(o2.getName());
    }

    private static Map<String, Integer> sortMap = new HashMap<>();

    {
        sortMap.put("第一章", 1);
        sortMap.put("第二章", 2);
        sortMap.put("第三章", 3);
        sortMap.put("第四章", 4);
        sortMap.put("第五章", 5);
        sortMap.put("第六章", 6);
        sortMap.put("第七章", 7);
        sortMap.put("第八章", 8);
        sortMap.put("第九章", 9);
        sortMap.put("第十章", 10);
        sortMap.put("第十一章", 11);

    }

    public int isSpe(String name) {
        for (Map.Entry<String, Integer> entry : sortMap.entrySet()) {
            if (name.startsWith(entry.getKey())) {
                return entry.getValue();
            }
        }
        return -1;
    }
}
