package enuemRecordTest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;

public class RecordJsonUtil {
    public static ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    public static String writeValueAsString(Object entity) {
        try {
            return objectMapper.writeValueAsString(entity);
        } catch (Exception e) {
            return null;
        }
    }

    public static <T> T readValue(String josnStr, Class<T> cls) {
        try {
            return (T) objectMapper.readValue(josnStr, cls);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}

