package fnote.common;


import fnote.domain.config.FlyContext;
import org.apache.commons.lang3.StringUtils;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by qryc on 2021/9/22
 */
public abstract class StorePathService {
    /// 路径配置
    public static final String PATH_ARTICLE = "article";
    public static final String PATH_USER = "user";
    public static final String PATH_BACK = "back";
    public static final String PATH_CUSTOM = "flyCustomRootPath";
    private Path rootDirPath;
    private Path tempDirPath;
    private Map<String, String> storeDirStrMap;
    //customDocPath
    protected Map<String, String> customPathMap;

    public static void main(String[] args) {
//        Path path = Path.of("/Volumes/HomeWork/1-Fly/FlyPic");
//        Path path2 = path.resolve("/PicBed/业务监控/行业分析/2023-03-16_14-26-17.png");
//        System.out.println(path2);
        System.out.println("".split(",").length);
        System.out.println(Arrays.stream("".split(",")).collect(Collectors.toList()).size());
        System.out.println(Arrays.stream("".split(",")).collect(Collectors.toList()));


    }

    public Path getRootDirPath(String storeName) {
        return rootDirPath;
    }

    public Path getUserDirPath(String pin) {
        return rootDirPath.resolve(pin);
    }

    /**
     * 文章路径，或者自定义，或内定
     */
    public List<Path> getUserArticleDirPaths(String pin) {
        return Stream.of(
                getUserDirPath(pin).resolve(PATH_ARTICLE)
        ).toList();
    }

    public Path getArticleDefaultPath(String pin, String userLabel) {
        return getUserDirPath(pin).resolve(PATH_ARTICLE).resolve("default");
    }


    public Path getTempDirPath(String storeName) {
        return tempDirPath;
    }


    public Path getUTempRootPath(String pin) {
        return tempDirPath.resolve(pin);
    }


    private Path getUDirPath(String storeName, String pin) {
        String storeDirStr = storeDirStrMap.get(storeName);
        if (StringUtils.isBlank(storeDirStr)) {
            throw new UnsupportedOperationException("directDir getUDirPath");
        } else {
            return getUserDirPath(pin).resolve(storeDirStr);
        }
    }


    public boolean isInStoreDir(Path absolutePath) {
        return true;
//        for (PathConfigModel configModel : storeNameToPCM.values()) {
//            if (absolutePath.toString().startsWith(configModel.getRootPtah().toString())) {
//                return true;
//            }
//        }
//        return false;
    }

    /**
     * 图片路径，包含文章下的图片，以及图床
     */
    public List<Path> getUserPicDirPaths(String pin) {
        return Stream.of(
                getUserDirPath(pin).resolve(PATH_ARTICLE).resolve("pic")
        ).toList();
    }


    public List<Path> getUserDiskDirPaths(FlyContext flyContext) {
        if (flyContext.isAdmin()) {
            return List.of(Path.of(System.getProperty("user.home")));
        }
        //用户未配置，返回用户默认
        return Stream.of(this.getUDirPath(PATH_ARTICLE, flyContext.getPin())).toList();
    }

    public List<Path> getFlyDiskCanSelect(FlyContext flyContext) {
        return getUserDiskDirPaths(flyContext);
    }

    public void setStoreDirStrMap(Map<String, String> storeDirStrMap) {
        this.storeDirStrMap = storeDirStrMap;
    }


    public void setRootDirPath(String rootDirPath) {
        this.rootDirPath = Path.of(PropertisUtil.adjustPath(rootDirPath));
    }

    public void setTempDirPath(String tempDirPath) {
        this.tempDirPath = Path.of(PropertisUtil.adjustPath(tempDirPath));
    }

    public void setCustomPathMap(Map<String, String> customPathMap) {
        this.customPathMap = customPathMap;
    }
}
