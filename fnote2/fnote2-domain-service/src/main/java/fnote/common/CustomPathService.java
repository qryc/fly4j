package fnote.common;

import fnote.domain.config.FlyContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CustomPathService extends StorePathService {
    protected Map<String, String> user2CustomRoot = new HashMap<>();


    public void init() {

       //todo 解析配置文件


    }


    @Override
    public List<Path> getAllArticleDirPaths(String pin) {
        List<String> customPaths = this.getUserCustomPaths(pin, customDocPath);
        if (CollectionUtils.isEmpty(customPaths)) {
            //默认路径
            return super.getAllArticleDirPaths(pin);
        } else {
            //如果有自定义路径，优先返回自定义路径
            return customPaths.stream()
                    .map(str -> Path.of(str)).toList();

        }
    }


    @Override
    public List<Path> getFlyDiskCanSelect(FlyContext flyContext) {
        return this.getUserCustomPaths(flyContext.getPin(), customDiskPath).stream().map(str -> Path.of(str))
                .toList();
    }

    /**
     * 图片路径，包含文章下的图片，以及图床
     */
    @Override
    public List<Path> getUserPicDirPaths(String pin) {
        //用户默认图片路径
        if (StringUtils.isBlank(customPicBedPath.get(pin))) {
            return super.getUserPicDirPaths(pin);
        } else {
            //自定义图床
            return Stream.of(Path.of(PropertisUtil.adjustPath(customPicBedPath.get(pin)))).toList();
        }


    }

    public List<String> getUserCustomPaths(String pin, Map<String, String> pathMap) {
        if (StringUtils.isBlank(pathMap.get(pin))) {
            return new ArrayList<>();
        }
        return Arrays.stream(pathMap.get(pin).split(",")).map(str -> PropertisUtil.adjustPath(str)).toList();
    }

    public List<Path> getUserDiskDirPaths(FlyContext flyContext) {


        //根据用户的自定义路径，叠加过滤
        List<String> customPaths = this.getUserCustomPaths(flyContext.getPin(), customDiskPath);
        if (CollectionUtils.isEmpty(customPaths)) {
            //用户未配置，返回用户默认
            return super.getUserDiskDirPaths(flyContext);
        } else {
            //返回用户自定义配置
            return customPaths.stream()
                    .filter(pathStr -> flyContext.clientConfig().getShowFolderList().contains(pathStr))
                    .map(str -> Path.of(str))
                    .collect(Collectors.toList());
        }


    }
}
