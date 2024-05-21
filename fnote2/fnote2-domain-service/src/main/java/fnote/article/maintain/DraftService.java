package fnote.article.maintain;

import java.util.HashMap;
import java.util.Map;

public class DraftService {
    private static final Map<String,String> draftMap=new HashMap<>();


    public static void setDraft(String pin, String draftStr) {
        draftMap.put(pin, draftStr);
    }

    public static String getDraft(String pin) {
        return draftMap.get(pin);
    }

}
