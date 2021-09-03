package fly4j.common.back;

import java.util.List;

public interface DirZipConfig {
    List<ZipConfig> getZipConfigList();


    void reSetZipConfigs(List<ZipConfig> zipConfigs);

    boolean isdeleteDir(String path);

}
