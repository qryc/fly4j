package fly4j.common.back.version;

/**
 * Created by qryc on 2021/9/3
 */
public record FileDigestModel(String pathStr, Long length, String md5) {
}
