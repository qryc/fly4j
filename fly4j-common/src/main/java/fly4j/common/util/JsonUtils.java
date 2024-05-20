package fly4j.common.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

/**
 * Created by qryc on 2015/7/24.
 */
public class JsonUtils {
    static final Logger log = LoggerFactory.getLogger(JsonUtils.class);
    public static final String DATEFORMAT = "yyyy-MM-dd HH:mm:ss";
    public static ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

    }

    /**
     * 可以处理Map,读Map要注意，读取使用readStringStringHashMap
     * @param entity
     * @return
     */
    public static String writeValueAsString(Object entity) {
        try {
            return objectMapper.writeValueAsString(entity);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
            //throw new RepocitoryCallException(RESULT_CODE.ERROR_READ_JSON);

        }
    }

    public static <T> T readValue(String josnStr, Class<T> cls) {
        if (StringUtils.isEmpty(josnStr)) {
            return null;
        }
        try {
            return (T) objectMapper.readValue(josnStr, cls);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Map<String, String> readStringStringHashMap(String md5Str) throws IOException {
        TypeReference<Map<String, String>> typeRef
                = new TypeReference<Map<String, String>>() {
        };
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(md5Str, typeRef);
    }
}
