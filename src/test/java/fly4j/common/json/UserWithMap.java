package fly4j.common.json;

import java.util.Map;

/**
 * Created by qryc on 2021/8/31
 */
class UserWithMap {
    private String name;
    private Map<String, String> map;

    @Override
    public String toString() {
        return "UserWithMap{" +
                "name='" + name + '\'' +
                ", map=" + map +
                '}';
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setMap(Map<String, String> map) {
        this.map = map;
    }

    public Map<String, String> getMap() {
        return map;
    }
}
