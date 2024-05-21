package fnote.user.listener;

import fnote.domain.config.FlyConfig;
import fnote.user.domain.service.DeployService;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * @author qryc 12/19/2020
 */
public class BootInfoLogger implements ApplicationListener<ContextRefreshedEvent> {
    protected DeployService deployService;
    private static boolean switchSplitFlag = true;

    public static void out(String pre, Object msg, int size) {
        System.out.print(pre);
        for (int i = 0; i < size - pre.length(); i++) {
            System.out.print(switchSplitFlag ? " " : "-");
        }
        System.out.println(msg.toString());
        switchSplitFlag = !switchSplitFlag;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if (contextRefreshedEvent.getApplicationContext().getParent() == null) {//保证只执行一次
            //启动的时候打印程序相关目录
            BootInfoLogger.out("OnLine:", FlyConfig.onLine, 20);
//            BootInfoLogger.out("HomePath:", FlyConfig.getHomePath(), 20);
//            BootInfoLogger.out("TempPath:", FlyConfig.getTempPath(), 20);
//            BootInfoLogger.out("UserDirPath:", FlyConfig.getUserHome4Data("${userName}"), 20);
//            BootInfoLogger.out("BackDataDirPath:", FlyConfig.getUserDir4Data("${userName}"), 20);
//            BootInfoLogger.out("UserInfoDirPath:", StorePathService.USER.getStoreFilePathWithPin("${userName}"), 20);
//            BootInfoLogger.out("UserZipDataPath:", FlyConfig.getUserDir4Back("${userName}"), 20);
//            try {
//                if (deployService.getDeployConfig() != null && deployService.getDeployConfig().getSysEmailConfig() != null)
//                    BootInfoLogger.out("emailConfig:", deployService.getDeployConfig().getSysEmailConfig().getFromEmail(), 20);
//            } catch (RepositoryException e) {
//                e.printStackTrace();
//            }
        }
    }

    public void setDeployService(DeployService deployService) {
        this.deployService = deployService;
    }
}
