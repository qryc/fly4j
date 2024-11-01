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
    private String customPath;

    public void init() {
        String uname = customPath.split(",")[0];
        String path = customPath.split(",")[1];
        user2CustomRoot.put(uname, path);
        //todo 解析配置文件


    }


    @Override
    public List<Path> getAllArticleDirPaths(String pin) {
        if (StringUtils.isEmpty(user2CustomRoot.get(pin))) {
            //默认路径
            return super.getAllArticleDirPaths(pin);
        } else {
            //如果有自定义路径，优先返回自定义路径
            Path path = Path.of(user2CustomRoot.get(pin));
            return List.of(path);

        }
    }


    @Override
    public List<Path> getFlyDiskCanSelect(FlyContext flyContext) {
        Path path = Path.of(user2CustomRoot.get(flyContext.getPin()));
        return List.of(path);
    }

    /**
     * 图片路径，包含文章下的图片，以及图床
     */
    @Override
    public List<Path> getUserPicDirPaths(String pin) {
        Path path = Path.of(user2CustomRoot.get(pin));
        return List.of(path);


    }

    public List<Path> getUserDiskDirPaths(FlyContext flyContext) {

        Path path = Path.of(user2CustomRoot.get(flyContext.getPin()));
        return List.of(path);


    }

    public void setCustomPath(String customPath) {
        this.customPath = customPath;
    }
}
