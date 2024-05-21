package flynote.application.manual;

import fnote.domain.config.LogConst;
import org.apache.commons.lang3.StringUtils;
import org.markdown4j.Markdown4jProcessor;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * 读取classPath下的manual-addBlog.txt和manual.md
 * Created by qryc on 2017/10/4.
 */
public class ManualByFile implements ManualService {
    private static String addBlogManual;
    private static String manual;

    static {
        try {
            //添加博客的帮助文档
            var addBlogPath = Path.of(ManualByFile.class.getResource("/manual/manual-addBlog.txt").toURI());
            addBlogManual = Files.readString(addBlogPath).replaceAll(StringUtils.CR, "").replaceAll(StringUtils.LF, "\\\\n");
            /**
             * 生成普通使用手册的帮助文档
             */
            //得到普通使用手册的帮助文档的MD文本

            var manualPath = Path.of(ManualByFile.class.getResource("/manual/manual.md").toURI());
            manual = Files.readString(manualPath);
            manual = new Markdown4jProcessor().process(manual);
        } catch (Exception e) {
            LoggerFactory.getLogger(LogConst.FILE_EXCEPTION).error("Markdown4jProcessor manual exception ", e);
        }
    }

    public static void main(String[] args) {
        System.out.println(ManualByFile.addBlogManual);
    }

    @Override
    public String getAddArticleManual() {
        return addBlogManual;
    }

    @Override
    public String getManual() {
        return manual;
    }
}
