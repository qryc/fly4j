package flynote.application.analysis;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by qryc on 2020/4/4.
 */
public class Histogram {
    private final Map<String, Integer> map = new LinkedHashMap<>();

    public void add(Object key, Integer value) {
        map.put(key.toString(), value);
    }

    public String getX() {
        StringBuilder xStr = new StringBuilder();
        map.forEach((k, v) -> {
            xStr.append("'").append(k).append("',");
        });
        if (xStr.length() > 1)
            xStr.deleteCharAt(xStr.length() - 1);
        return xStr.toString();
    }

    public String getY() {
        StringBuilder yStr = new StringBuilder();
        map.forEach((k, v) -> {
            yStr.append(v).append(",");
        });
        if (yStr.length() > 1)
            yStr.deleteCharAt(yStr.length() - 1);
        return yStr.toString();
    }

    public String getEchartPie() {
        StringBuilder xStr = new StringBuilder();
        map.forEach((k, v) -> {
            xStr.append("{value:").append(v).append(",spaceName: '").append(k + "-" + v).append("'},");
        });
        if (xStr.length() > 1)
            xStr.deleteCharAt(xStr.length() - 1);
        return xStr.toString();
    }
}
