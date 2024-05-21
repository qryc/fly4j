package fnote.domain.config;

import fly4j.common.http.CookiesUtil;
import fly4j.common.http.WebUtil;
import fly4j.common.util.JsonUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by qryc on 2016/12/27.
 */
public class PageConfigUtil {

    public static RequestConfig getRequestConfig(HttpServletRequest req) {
        return new RequestConfig()
                .setMobileSite(WebUtil.isFromPhone(req))
                .setUuid("" + System.currentTimeMillis())
                .setDomainUrl(WebUtil.getDomainUrl(req))
                .setServerName(("FlyNote"));
    }

    public static ClientConfig getClientConfig(HttpServletRequest req, HttpServletResponse resp) {
        var cookieValue = CookiesUtil.getCookieValueDecode(req, "pageConfig");
        if (StringUtils.isBlank(cookieValue)) {
            saveClientConfig(req, resp, new ClientConfig());
            return new ClientConfig();
        }
        cookieValue = CookiesUtil.getCookieValueDecode(req, "pageConfig");
        return JsonUtils.readValue(cookieValue, ClientConfig.class);


    }

    public static void saveClientConfig(HttpServletRequest req, HttpServletResponse resp, ClientConfig config) {
        CookiesUtil.addHttpOnlyCookieEncode(req, resp, "pageConfig",
                JsonUtils.writeValueAsString(config), 10000000);
    }
}
