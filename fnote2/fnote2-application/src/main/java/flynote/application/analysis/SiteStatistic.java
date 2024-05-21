package flynote.application.analysis;

/**
 * 网站内容分析结果
 *
 * @author xxgw
 */
public class SiteStatistic {
    private Integer articlesSize;
    //文件数量
    private int fileNum;
    private int fileNumImg;
    private int fileNumOther;
    //文件大小
    private long fileSize;
    private long fileSizeImg;
    private long fileSizeOther;


    public Integer getArticlesSize() {
        return articlesSize;
    }

    public SiteStatistic setArticlesSize(Integer blogSize) {
        this.articlesSize = blogSize;
        return this;
    }



    public int getFileNum() {
        return fileNum;
    }

    public SiteStatistic setFileNum(int fileNum) {
        this.fileNum = fileNum;
        return this;
    }

    public int getFileNumImg() {
        return fileNumImg;
    }

    public SiteStatistic setFileNumImg(int fileNumImg) {
        this.fileNumImg = fileNumImg;
        return this;
    }

    public int getFileNumOther() {
        return fileNumOther;
    }

    public SiteStatistic setFileNumOther(int fileNumOther) {
        this.fileNumOther = fileNumOther;
        return this;
    }

    public long getFileSize() {
        return fileSize;
    }

    public SiteStatistic setFileSize(long fileSize) {
        this.fileSize = fileSize;
        return this;
    }

    public long getFileSizeImg() {
        return fileSizeImg;
    }

    public SiteStatistic setFileSizeImg(long fileSizeImg) {
        this.fileSizeImg = fileSizeImg;
        return this;
    }

    public long getFileSizeOther() {
        return fileSizeOther;
    }

    public SiteStatistic setFileSizeOther(long fileSizeOther) {
        this.fileSizeOther = fileSizeOther;
        return this;
    }

}
