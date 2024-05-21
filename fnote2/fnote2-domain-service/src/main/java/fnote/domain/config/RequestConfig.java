package fnote.domain.config;

/**
 * 页面展示设置，会设置到springMvc上下文，每个vm都可以访问
 * 页面内容的来源有:(cookies,请求参数，配置)
 * 设置：如果request的参数中有传pageConfig的属性，则在过滤器中写cookies
 * 读取：过滤器中取得cookie，把属性值设置到request域中，可以在页面直接使用 #if(${pageConfig.fullScreen}=="true")
 * <p>
 * <p>
 * Created by qryc on 2016/8/15.
 */
public class RequestConfig {

    /**
     * 是否手机端，不存cookie
     */
    private boolean mobileSite = false;
    /**
     * 服务器名称
     */
    private String serverName;
    /**
     * 网站地址
     */
    private String uuid;
    private String domainUrl;

    @Override
    public String toString() {
        return "RequestConfig{" +
                "mobileSite=" + mobileSite +
                ", serverName='" + serverName + '\'' +
                ", uuid='" + uuid + '\'' +
                ", domainUrl='" + domainUrl + '\'' +
                '}';
    }

    public boolean isPc() {
        return !mobileSite;
    }

    public boolean isMobileSite() {
        return mobileSite;
    }

    public RequestConfig setMobileSite(boolean mobileSite) {
        this.mobileSite = mobileSite;
        return this;
    }

    public String getServerName() {
        return serverName;
    }

    public RequestConfig setServerName(String serverName) {
        this.serverName = serverName;
        return this;
    }

    public String getUuid() {
        return uuid;
    }

    public RequestConfig setUuid(String uuid) {
        this.uuid = uuid;
        return this;
    }

    public String getDomainUrl() {
        return domainUrl;
    }

    public RequestConfig setDomainUrl(String domainUrl) {
        this.domainUrl = domainUrl;
        return this;
    }
}
