package fnote.article.share;

import farticle.domain.entity.CplArticle;
import farticle.domain.entity.WorkSpaceParam;
import fly4j.common.cache.FlyCache;
import fly4j.common.util.RepositoryException;
import flynote.article.query.ArticleLoad;
import fnote.user.domain.entity.IdPin;
import org.apache.commons.lang3.StringUtils;


/**
 * Created by qryc on 2017/10/2.
 */
public class AuthShareServiceImpl implements AuthShareService {

    private FlyCache<Object> authShareCache;
    private ArticleLoad articleLoad;

    public static class ShareArticleInfo {
        private CplArticle publishedArticle;
        private String pin;
        private Long ttlMilliSeconds;
        private String shareCode;

        public CplArticle getPublishedArticle() {
            return publishedArticle;
        }

        public ShareArticleInfo setPublishedArticle(CplArticle publishedArticle) {
            this.publishedArticle = publishedArticle;
            return this;
        }

        public String getPin() {
            return pin;
        }

        public ShareArticleInfo setPin(String pin) {
            this.pin = pin;
            return this;
        }

        public Long getTtlMilliSeconds() {
            return ttlMilliSeconds;
        }

        public ShareArticleInfo setTtlMilliSeconds(Long ttlMilliSeconds) {
            this.ttlMilliSeconds = ttlMilliSeconds;
            return this;
        }

        public String getShareCode() {
            return shareCode;
        }

        public ShareArticleInfo setShareCode(String shareCode) {
            this.shareCode = shareCode;
            return this;
        }
    }


    /**
     * 添加博客的资源共享码双向索引
     */
    @Override
    public void shareArticle(Long id, String pin,String encryptPwd, WorkSpaceParam workSpaceParam) throws RepositoryException {
//        var idPin = IdPin.of(id, pin);
//        var shareCode = RandomStringUtils.random(8, true, true);
//        var idPinStr = getPinnoteFileStrStr(idPin);
//        authShareCache.put(idPinStr, shareCode, TimeUnit.DAYS.toMillis(1));
//        var cplArticle = articleLoad.getCplArticle4View(idPin, encryptPwd, workSpaceParam);
//        var articleView = ArticleViewFactory.createArticleView(cplArticle, flyContext.isMobileSite());
////        var content = (String) articleHo.getExtValue(ACConst.AEM_HTML_CONTENT);
//        var content = articleView.getContent();
//        PublishedArticle publishedArticle = new PublishedArticle()
//                .setCode(shareCode)
//                .setTitle(articleView.getTitle())
//                .setContent(content);
//        authShareCache.put(shareCode, cplArticle, TimeUnit.DAYS.toMillis(1));
//        authShareCache.put(shareCode + "-pin", pin, TimeUnit.DAYS.toMillis(1));
    }


    @Override
    public ShareArticleInfo getShareArticleInfo(IdPin idPin) {
        var shareCode = (String) authShareCache.get(getPinnoteFileStrStr(idPin));
        return getShareArticleInfo(shareCode);
    }

    /**
     * 得到博客的Id，以及剩余的生命期
     */
    @Override
    public ShareArticleInfo getShareArticleInfo(String shareCode) {
        if (StringUtils.isBlank(shareCode)) {
            return null;
        }
        try {
            var article = (CplArticle) authShareCache.get(shareCode);
            if (null == article) {
                return null;
            }
            var pin = (String) authShareCache.get(shareCode + "-pin");
            Long articleShareTtl = authShareCache.ttl(shareCode);
            return new ShareArticleInfo()
                    .setPublishedArticle(article)
                    .setTtlMilliSeconds(articleShareTtl)
                    .setShareCode(shareCode)
                    .setPin(pin);
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }

    }

    private String getPinnoteFileStrStr(IdPin idPin) {
        return idPin.getPin() + "," + idPin.getNoteFileStr();
    }

    public void setAuthShareCache(FlyCache authShareCache) {
        this.authShareCache = authShareCache;
    }

    public void setArticleLoad(ArticleLoad articleLoad) {
        this.articleLoad = articleLoad;
    }
}
