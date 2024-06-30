package fnote.infrastructure.impl.file;

import farticle.domain.entity.*;
import fly4j.common.domain.IExtMap;
import fly4j.common.file.FileUtil;
import fly4j.common.util.*;
import farticle.domain.infrastructure.ArticleRepository;
import fnote.domain.config.FlyContext;
import fnote.user.domain.entity.BaseDomain;
import fnote.user.domain.entity.IdPin;
import fnote.common.StorePathService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

public class ArticleNoteRepositoryFile implements ArticleRepository {
    private static final Logger log = LoggerFactory.getLogger(ArticleNoteRepositoryFile.class);
    //控制修改的时候是否使用缓存
    private StorePathService pathService;
    private static final String PATH_ARTICLE = "article";
    private static final String PATH_DRAFT = "draft";
    private static final String MD_SPLIT_START = "<!-- " + "-~".repeat(10);
    private static final String MD_SPLIT_END = "-->";

    private final int maxArticleCount = 1000;


    private record PinSpace(String pin, String workPath) {
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PinSpace pinSpace = (PinSpace) o;
            return pin.equals(pinSpace.pin) && workPath.equals(pinSpace.workPath);
        }

        @Override
        public int hashCode() {
            return Objects.hash(pin, workPath);
        }

        public static PinSpace of(String pin, String wordSpace) {
            return new PinSpace(pin, wordSpace);
        }
    }


    private Path getDraftFilePath(String pin, String filename, Long id) {
        if (null != id) {
            return pathService.getUTempRootPath(pin).resolve("draft").resolve(DateUtil.getDateStr4Name(new Date()) + filename);
        } else {
            return pathService.getUTempRootPath(pin).resolve("draft").resolve(DateUtil.getDateStr4Name(new Date()) + filename);
        }
    }


    private String replaceFileName(String fileName) {
        return fileName.replaceAll("\\*", "").replaceAll("\\\\", "").replaceAll("_", "");
    }

    private String getFileName(CplArticle cplArticle) {
        String fileName = cplArticle.getArticleContent().title()
                .replaceAll("\\*", "").replaceAll("\\\\", "").replaceAll("_", "")
                .replaceAll("#", "")
//                .replaceAll(" ", "")
                .trim();
        if (cplArticle.getArticleContent().authEnum().equals(ArticleAuthEnum.REAL_OPEN)) {
            return fileName + ".f.md";
        } else {
            return fileName + ".flyNote";
        }
    }

    private String getUpdateFileName(CplArticle cplArticle, File file) {
        String fileName = FileUtil.getNameWithoutSuffix(file);
        if (file.getName().endsWith(".f.md")) {
            fileName = file.getName().substring(0, file.getName().length() - 5);
        }
        if (cplArticle.getArticleContent().authEnum().equals(ArticleAuthEnum.REAL_OPEN)) {
            return fileName + ".f.md";
        } else {
            return fileName + ".flyNote";
        }
    }

    @Override
    public List<CplArticle> findCplArticlesByPin(String pin, Path rootPath, Function<CplArticle, CplArticle> function) throws RepositoryException {
        var articles = new ArrayList<CplArticle>();
        Predicate<File> refusePredicate = new Predicate<File>() {
            @Override
            public boolean test(File file) {
                if (file.getName().equals("draft")) {
                    return true;
                }
                return false;
            }
        };
        FileUtil.walkAllFile(rootPath.toFile(), refusePredicate, file -> ExceptionUtil.wrapperRuntime(() -> {
            //通过扩展名来识别文章
            var fname = file.getName();

            //解析文章
            if (fname.endsWith(".flyNote") || fname.endsWith(".md")) {
                CplArticle cplArticle = analysisCplArticle(file, pin);
                if (null != function) {
                    //执行过滤链过滤
                    cplArticle = function.apply(cplArticle);
                    if (null != cplArticle) {
                        articles.add(cplArticle);
                    }
                } else {
                    //无过滤链直接加入
                    articles.add(cplArticle);
                }
            }
        }));

        //按开始时间排序
        Collections.sort(articles, (a1, a2) -> a1.getCreateTime().compareTo(a2.getCreateTime()));
        return articles;
    }


    private List<CplArticle> cloneList(List<CplArticle> list) {
        ArrayList<CplArticle> cloneList = new ArrayList<>();
        list.forEach(cplArticle -> ExceptionUtil.wrapperRuntime(() -> cloneList.add(cplArticle.clone())));
        return cloneList;
    }


    @Override
    public String insertCplArticle(CplArticle cplArticle) throws RepositoryException {
        return RepositoryException.wrapperR(() -> {
            //如果没有ID，设置ID
            if (cplArticle.getExtId() == null) {
                cplArticle.setExtId(System.currentTimeMillis());
            }


            /**计算保存路径*/
            Path articlePath = null;
            var fileName = getFileName(cplArticle);
            String pin = cplArticle.getPin();
            String currentWorkRootPathStr = FlyContext.getCurrentWorkRootPath(cplArticle.getPin());
            if (StringUtils.isEmpty(currentWorkRootPathStr)) {
                //没有指定目录，保存默认目录
                articlePath = pathService.getArticleDefaultPath(pin, "").resolve(fileName);
            } else {
                articlePath = Path.of(currentWorkRootPathStr).resolve(fileName);
            }

            //保存博客
            String articleJson = getArticleJson(cplArticle);
            if (Files.exists(articlePath)) {
                throw new RepositoryException("article already exsits:" + articlePath);
            }
            if (Files.notExists(articlePath.getParent()))
                Files.createDirectories(articlePath.getParent());
            Files.writeString(articlePath, articleJson);
            cplArticle.setNoteFileStr(articlePath.toString());
            return articlePath.toString();
        });

    }


    @Override
    public void deleteCplArticleById(IdPin idPin) throws RepositoryException {

        RepositoryException.wrapper(() -> {
            var path = Path.of(idPin.getNoteFileStr());
            draft(idPin.getPin(), path.toFile(), null);
            Files.deleteIfExists(path);
        });

    }

    public boolean isFile(String fileName) {
        return fileName.startsWith("") && fileName.endsWith("");
    }

    public void draft(String pin, File file, Long id) throws Exception {
        if (StringUtils.isNotBlank(pin)) {
            Path draftPath = getDraftFilePath(pin, file.getName(), id);
            if (Files.notExists(draftPath.getParent()))
                Files.createDirectories(draftPath.getParent());
            Files.writeString(draftPath, Files.readString(file.toPath()));
        }
    }

    @Override
    public void updateCplArticle(CplArticle cplArticle) throws RepositoryException {
        RepositoryException.wrapper(() -> {
            if (cplArticle.getExtId() == null) {
                cplArticle.setExtId(System.currentTimeMillis());
            }
            var path = Path.of(cplArticle.getNoteFileStr());
            //从文件系统更新
            File file = path.toFile();

//            写入草搞
            draft(cplArticle.getPin(), file, cplArticle.getExtId());

            //再次匹配ID，防止误修改
            String articleJson = getArticleJson(cplArticle);
            String newStoreFileName = getUpdateFileName(cplArticle, file);
            //文件名不变，直接覆盖写文件
            if (newStoreFileName.equals(file.getName())) {
                Files.writeString(file.toPath(), articleJson);
                cplArticle.setNoteFileStr(file.toPath().toString());
            } else {//文件名改变，重命名文件，再覆盖写文件
                var newFilePath = file.toPath().getParent().resolve(newStoreFileName);
                if (Files.exists(newFilePath)) {
                    throw new FileAlreadyExistsException(newFilePath.toString());
                }
                Files.move(file.toPath(), newFilePath);
                Files.writeString(newFilePath, articleJson);
                cplArticle.setNoteFileStr(newFilePath.toString());
            }
        });


    }

    private static String getArticleJson(CplArticle cplArticle) {
        var articleDo = new ArticleDo(cplArticle);
        var articleJson = JsonUtils.writeValueAsString(articleDo);
        if (cplArticle.getArticleContent().authEnum().equals(ArticleAuthEnum.REAL_OPEN)) {
            //非加密专有格式
            var articleJsonBuilder = new StringBuilder();
            articleJsonBuilder.append(cplArticle.getArticleContent().title()).append(StringConst.LF);
            articleJsonBuilder.append(cplArticle.getArticleContent().content()).append(StringConst.LF);

            //拼接.f.md文件的补充信息
            articleJsonBuilder.append(MD_SPLIT_START).append(StringConst.LF);
            //清除内容和标题
            articleDo.setContent("").setTitle("");
            articleJsonBuilder.append(JsonUtils.writeValueAsString(articleDo)).append(StringConst.LF);
            articleJsonBuilder.append(MD_SPLIT_END).append(StringConst.LF);

            articleJson = articleJsonBuilder.toString();

        }
        return articleJson;
    }


    /**
     * 不使用缓存内容，会使用缓存索引
     */
    @Override
    public CplArticle getCplArticleById4Edit(IdPin idPin) throws RepositoryException {
        if (null != idPin.getNoteFileStr()) {
            //通过文章路径获取
            return RepositoryException.wrapperR(() -> {
                var path = Path.of(idPin.getNoteFileStr());
                return analysisCplArticle(path.toFile(), idPin.getPin());

            });
        } else {
            throw new UnsupportedOperationException();
        }

    }

    private CplArticle analysisCplArticle(File file, String pin) throws Exception {
        String str = Files.readString(file.toPath());
        Path path = file.toPath();

        //flynote加密格式
        if (file.getName().endsWith(".flyNote") && str.startsWith("{")) {

            var articleDo = JsonUtils.readValue(str, ArticleDo.class);
            var cplArticle = articleDo.buildCplArticle(path);
            cplArticle.setFile(file);
            return cplArticle;
        }

        //.f.md格式
        if (file.getName().endsWith(".flyNote") && !str.startsWith("{") || file.getName().endsWith(".f.md")) {
            //从MD的附加信息解析组织信息，不带标题和内容
            String[] arrs = str.split(MD_SPLIT_START);
            String json = arrs[1].replaceAll(MD_SPLIT_END, "");
            var articleDo = JsonUtils.readValue(json, ArticleDo.class);
            var cplArticle = articleDo.buildCplArticle(path);

            //补充标题和内容
            cplArticle.setArticleContent(ArticleContent.buildArticleContent(cplArticle.getArticleContent().authEnum(), arrs[0], StringUtils.LF));
//            var indexTitle = arrs[0].indexOf(StringUtils.LF);
//            if (indexTitle == -1) {
//                //如果没有换行符，使用全部做标题
//                cplArticle.setArticleContent(cplArticle.getArticleContent().of(arrs[0], ""));
//            } else {
//                //根据内容截断第一行为标题,剩下的为内容
//                var title = arrs[0].substring(0, indexTitle).trim();
//                //截断内容
//                var content = arrs[0].substring(indexTitle + 1);
//                //截断空行
//                if (content.endsWith(StringConst.LF)) {
//                    content = content.substring(0, content.length() - 1);
//                }
//                cplArticle.setArticleContent(cplArticle.getArticleContent().of(title, content));
//            }
            cplArticle.setFile(file);
            return cplArticle;

        }

        //文本文件解析
        if (!FileUtil.isImg(file)) {
            BasicFileAttributes attrs = Files.readAttributes(path, BasicFileAttributes.class);

            ArticleContent articleContent = ArticleContent.buildArticleContent(ArticleAuthEnum.REAL_OPEN, str, StringUtils.LF);
            CplArticle cplArticle = new CplArticle(articleContent, null)
                    .setPin(pin)
                    .setCreateTime(new Date(attrs.creationTime().toMillis()))
                    .setModifyTime(new Date(file.lastModified()));
            var articleOrganize = new ArticleOrganize()
                    .setFileType("other")
                    .setTextType(ArticleOrganize.TYPE_MD)
                    .setEditMode(ArticleOrganize.EDITMODE_EDIT)
                    .setMaturity(100);
            cplArticle.setArticleOrganize(articleOrganize);
            cplArticle.setNoteFileStr(path.toString());
            cplArticle.setType("md");
            if (file.getName().endsWith(".md")) {
                articleOrganize.setMdEditor(ArticleOrganize.ORGANIZE_MDEDITOR_CK);
            }
            cplArticle.setFile(file);
            return cplArticle;
        }
        throw new RuntimeException("不支持的文件格式");


    }

    @Override
    public CplArticle getCplArticleById4View(IdPin idPin) throws RepositoryException {
        return getCplArticleById4Edit(idPin);
    }

    @Override
    public CplArticle getCplArticleById4ViewById(String pin, Path rootPath, long id) throws RepositoryException {
        CplArticle result = null;
        Function<CplArticle, CplArticle> function = cplArticle -> {
            if (cplArticle.getExtId() != null && id == cplArticle.getExtId()) {
                return cplArticle;
            }
            return null;
        };
        List<CplArticle> cplArticles = this.findCplArticlesByPin(pin, rootPath, function);
        return cplArticles.size() > 0 ? cplArticles.get(0) : null;
    }

    /**
     * Created by qryc on 2020/2/8.
     */
    private static class ArticleDo extends BaseDomain<ArticleDo> {
        protected Long id;
        private int open;
        private String title;
        private String content;
        private ArticleOrganizeDo organizeDo;

        public ArticleDo() {
        }

        public ArticleDo(CplArticle cplArticle) {
            ArticleContent articleContent = cplArticle.getArticleContent();
            ArticleOrganizeDo articleOrganizeDo = new ArticleOrganizeDo()
                    .setEditor(cplArticle.getArticleOrganize().getTextType())
                    .setEditMode(cplArticle.getArticleOrganize().getEditMode())
                    .setMaturity(cplArticle.getArticleOrganize().getMaturity());
            articleOrganizeDo.getExtMap().put("workspace", cplArticle.getArticleOrganize().getWorkspace());
            articleOrganizeDo.getExtMap().put("topping", "" + cplArticle.getArticleOrganize().isTopping());
            articleOrganizeDo.getExtMap().put("insurance", "" + cplArticle.getArticleOrganize().isInsurance());
            articleOrganizeDo.getExtMap().put("mdEditor", cplArticle.getArticleOrganize().getMdEditor());

            this.setId(cplArticle.getExtId())
                    .setPin(cplArticle.getPin())
                    .setCreateTime(cplArticle.getCreateTime())
                    .setModifyTime(cplArticle.getModifyTime())
                    .setOpen(articleContent.authEnum().getAuthCode())
                    .setTitle(articleContent.title())
                    .setContent(articleContent.content())
                    .setOrganizeDo(articleOrganizeDo);
        }

        public CplArticle buildCplArticle(Path path) {
            ArticleContent articleContent = new ArticleContent(ArticleAuthEnum.getInsByAuthCode(this.open), title, content);
            CplArticle cplArticle = new CplArticle(articleContent, null)
                    .setPin(this.getPin())
                    .setCreateTime(this.getCreateTime())
                    .setModifyTime(this.getModifyTime());
            var articleOrganize = new ArticleOrganize()
                    .setTextType(organizeDo.getEditor())
                    .setEditMode(organizeDo.getEditMode())
                    .setMaturity(organizeDo.getMaturity());
            articleOrganize.setWorkspace(organizeDo.getExtMap().get("workspace"));
            articleOrganize.setTopping(Boolean.valueOf(organizeDo.getExtMap().get("topping")));
            articleOrganize.setInsurance(Boolean.valueOf(organizeDo.getExtMap().get("insurance")));
            articleOrganize.setMdEditor(organizeDo.getExtMap().get("mdEditor"));
            cplArticle.setExtId(this.getId());
            cplArticle.setArticleOrganize(articleOrganize);
            cplArticle.setNoteFileStr(path.toString());
            return cplArticle;

        }

        public ArticleDo setId(Long id) {
            this.id = id;
            return this;
        }

        public int getOpen() {
            return open;
        }

        public ArticleDo setOpen(int open) {
            this.open = open;
            return this;
        }

        public String getTitle() {
            return title;
        }

        public ArticleDo setTitle(String title) {
            this.title = title;
            return this;
        }

        public String getContent() {
            return content;
        }

        public ArticleDo setContent(String content) {
            this.content = content;
            return this;
        }

        public void setOrganizeDo(ArticleOrganizeDo organizeDo) {
            this.organizeDo = organizeDo;
        }

        public ArticleOrganizeDo getOrganizeDo() {
            return organizeDo;
        }

        public Long getId() {
            return id;
        }
    }

    private static class ArticleOrganizeDo implements IExtMap<ArticleOrganizeDo> {
        // 成熟度
        private Integer maturity;
        // 父博客ID
        private Long parentId;
        // 编辑器选择
        private int editor;
        private int orderNum = 100;
        //0普通编辑 1 追加
        private int editMode;
        private Map<String, String> extMap = new HashMap<>();

        public Integer getMaturity() {
            return maturity;
        }

        public ArticleOrganizeDo setMaturity(Integer maturity) {
            this.maturity = maturity;
            return this;
        }

        @Deprecated
        public Long getParentId() {
            return parentId;
        }

        @Deprecated
        public ArticleOrganizeDo setParentId(Long parentId) {
            this.parentId = parentId;
            return this;
        }

        public int getEditor() {
            return editor;
        }

        public ArticleOrganizeDo setEditor(int editor) {
            this.editor = editor;
            return this;
        }

        public int getOrderNum() {
            return orderNum;
        }

        public ArticleOrganizeDo setOrderNum(int orderNum) {
            this.orderNum = orderNum;
            return this;
        }

        public int getEditMode() {
            return editMode;
        }

        public ArticleOrganizeDo setEditMode(int editMode) {
            this.editMode = editMode;
            return this;
        }

        @Override
        public Map<String, String> getExtMap() {
            return extMap;
        }

        public ArticleOrganizeDo setExtMap(Map<String, String> extMap) {
            this.extMap = extMap;
            return this;
        }

    }

    public void setPathService(StorePathService pathService) {
        this.pathService = pathService;
    }
}
