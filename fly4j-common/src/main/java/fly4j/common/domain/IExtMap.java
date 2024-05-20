package fly4j.common.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Map;

public interface IExtMap<T> {
    Map<String, String> getExtMap();


    @JsonIgnore
    default Boolean getExtBooleanValue(String key) {
        if (null == getExtMap().get(key)) {
            return false;
        }
        return Boolean.valueOf(getExtMap().get(key));
    }

    @JsonIgnore
    default String getExtStringValue(String key) {
        return getExtMap().get(key);
    }

    @JsonIgnore
    default String getExtStringValue(String key, String defaultValue) {
        return getExtMap().get(key) == null ? defaultValue : getExtMap().get(key);
    }

    @JsonIgnore
    default T setExtStringValue(String key, Object value) {
        if (null == value) {
            return (T) this;
        }
        this.getExtMap().put(key, value.toString());
        return (T) this;
    }

    @JsonIgnore
    default T putExtAll(Map<String, String> paramMap) {
        this.getExtMap().putAll(paramMap);
        return (T) this;
    }
}
