package fnote.article.web.controller;

import farticle.domain.entity.CplArticle;
import fly4j.common.file.FileUtil;
import fly4j.common.util.ExceptionUtil;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public class PicCache {
    public static Map<String, String> rel2abPathMap = new HashMap<>();

    public static void cachePic(File file) {
//        System.out.println("111"+file.getAbsolutePath());
        FileUtil.walkAllFile(file, null, f -> ExceptionUtil.wrapperRuntime(() -> {
            //解析文章
            if (FileUtil.isImg(f)) {
                String[] names=f.getAbsolutePath().split("/pic/");
                if(names.length>0){
                    rel2abPathMap.put("pic/"+names[names.length-1], f.getAbsolutePath());
                }
            }
        }));
    }

    public static String getAbsPath(String relPath) {
        return rel2abPathMap.get(relPath);
    }
    public static void main(String[] args) {
        String[] names="/articleMaintain/pic/layout/1.png".split("/");
        System.out.println("sss "+names[names.length-1]);
    }

}
