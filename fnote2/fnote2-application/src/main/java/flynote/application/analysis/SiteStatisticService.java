package flynote.application.analysis;

import farticle.domain.consts.FlyConst;
import farticle.domain.extension.query.ArticleQueryParam;
import farticle.domain.extension.query.QueryBuilder;
import farticle.domain.infrastructure.ArticleRepository;
import farticle.domain.view.ArticleView4List;
import fly4j.common.util.RepositoryException;
import flynote.article.query.ArticleQuery;
import fnote.domain.config.FlyContext;

import java.util.*;

public class SiteStatisticService {
    private ArticleRepository articleRepository;
    private ArticleQuery articleQuery;

    /**
     * 得到当前站点的内容分析结果
     */
    public SiteStatistic getSiteStatistic(String pin) throws RepositoryException {
        var siteStatistic = new SiteStatistic();
//        var articleList = articleRepository.findCplArticlesByPin(pin, WorkSpaceParam.of(""));
//        siteStatistic.setArticlesSize(articleList.size());
//        FileUtil.walkAllFile(NoteStorePathEnum.getUserDir4Data(pin).toFile(),null, file -> {
//            if (FileUtil.isImg(file)) {
//                //设置文件个数
//                siteStatistic.setFileNumImg(siteStatistic.getFileNumImg() + 1);
//                //设置文件大小
//                siteStatistic.setFileSizeImg(siteStatistic.getFileSizeImg() + file.length());
//            } else {
//                siteStatistic.setFileNumOther(siteStatistic.getFileNumOther() + 1);
//                //设置文件大小
//                siteStatistic.setFileSizeOther(siteStatistic.getFileSizeOther() + file.length());
//            }
//            //设置文件大小
//            siteStatistic.setFileSize(siteStatistic.getFileSize() + file.length());
//        });
        return siteStatistic;
    }

    public Histogram getArticleSizeGram(String pin) throws RepositoryException {
//        List<CplArticle> articleList = articleRepository.findCplArticlesByPin(pin, WorkSpaceParam.of(""));
//        Histogram histogram = new Histogram();
//        histogram.add("文章", articleList.size());
//        return histogram;
        return new Histogram();
    }

    public Histogram getArticleLengthGram(FlyContext flyContext, String encryptPwd) throws RepositoryException {
        ArticleQueryParam queryParam = QueryBuilder.newBuilder()
                .flyContext(flyContext)
                .buId(FlyConst.ACConst.BAQ_ALL)
                .buildArticleQuery();
        List<ArticleView4List> articleList = articleQuery.queryShortArticleViews(queryParam);
        Map<String, Integer> lengthCount = new LinkedHashMap<>();
        lengthCount.put("1百", 0);
        lengthCount.put("5百", 0);
        lengthCount.put("1K", 0);
        lengthCount.put("3K", 0);
        lengthCount.put("3K+", 0);
        articleList.forEach(cplArticle -> {
            var content = cplArticle.getNoteFileStr();
            if (content == null) {
                lengthCount.put("1百", lengthCount.get("1百") + 1);
            } else if (content.length() > 3000) {
                lengthCount.put("3K+", lengthCount.get("3K+") + 1);
            } else if (content.length() > 1000) {
                lengthCount.put("3K", lengthCount.get("3K") + 1);
            } else if (content.length() > 500) {
                lengthCount.put("1K", lengthCount.get("1K") + 1);
            } else if (content.length() > 100) {
                lengthCount.put("5百", lengthCount.get("5百") + 1);
            } else {
                lengthCount.put("1百", lengthCount.get("1百") + 1);
            }
        });
        Histogram histogram = new Histogram();
        lengthCount.forEach((length, count) -> histogram.add(length, count));
        return histogram;
    }

    public Histogram getArticleMaturityGram(FlyContext flyContext, String encryptPwd) throws RepositoryException {
        ArticleQueryParam queryParam = QueryBuilder.newBuilder()
                .flyContext(flyContext)
                .buId(FlyConst.ACConst.BAQ_ALL)
                .buildArticleQuery();
        List<ArticleView4List> articleList = articleQuery.queryShortArticleViews(queryParam);
        Map<Integer, Integer> maturityCountMap = new LinkedHashMap<>();
        articleList.forEach(articleHo -> {
//            Integer maturity = articleHo.getArticleOrganize().getMaturity() == null ? 0 : articleHo.getArticleOrganize().getMaturity();
//            maturityCountMap.put(maturity, maturityCountMap.computeIfAbsent(maturity, k -> 0) + 1);
        });
        Histogram histogram = new Histogram();
        List<Integer> maturitys = new ArrayList<>();
        maturitys.addAll(maturityCountMap.keySet());
        Collections.sort(maturitys);
        maturitys.forEach((maturity) -> histogram.add(maturity, maturityCountMap.get(maturity)));
        maturityCountMap.forEach((maturity, count) -> histogram.add(maturity, count));
        return histogram;
    }

    public Histogram getArticleYearCreateGram(String pin) throws RepositoryException {
//        List<CplArticle> articleList = articleRepository.findCplArticlesByPin(pin, WorkSpaceParam.of(""));
//        Map<String, Integer> yearCount = new LinkedHashMap<>();
//        articleList.forEach(cplArticle -> {
//            String year = DateUtil.getYearTwoStr(cplArticle.getCreateTime());
//            yearCount.put(year, yearCount.computeIfAbsent(year, k -> 0) + 1);
//        });
//        Histogram histogram = new Histogram();
//        List<String> years = new ArrayList<>();
//        years.addAll(yearCount.keySet());
//        Collections.sort(years);
//        years.forEach(year -> histogram.add(year, yearCount.get(year)));
//        return histogram;
        return new Histogram();
    }

    public Histogram getArticleYearModifyGram(String pin) throws RepositoryException {
//        List<CplArticle> articleList = articleRepository.findCplArticlesByPin(pin, WorkSpaceParam.of(""));
//        Map<String, Integer> yearCount = new LinkedHashMap<>();
//        articleList.forEach(cplArticle -> {
//            String year = (null == cplArticle.getModifyTime() ? DateUtil.getYearTwoStr(cplArticle.getCreateTime()) : DateUtil.getYearTwoStr(cplArticle.getModifyTime()));
//            yearCount.put(year, yearCount.computeIfAbsent(year, k -> 0) + 1);
//        });
//        Histogram histogram = new Histogram();
//        List<String> years = new ArrayList<>();
//        years.addAll(yearCount.keySet());
//        Collections.sort(years);
//        years.forEach((year) -> histogram.add(year, yearCount.get(year)));
//        return histogram;
        return new Histogram();
    }

    public void setArticleRepository(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public void setArticleQuery(ArticleQuery articleQuery) {
        this.articleQuery = articleQuery;
    }

}
