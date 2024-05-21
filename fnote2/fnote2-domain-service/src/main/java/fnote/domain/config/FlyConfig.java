package fnote.domain.config;

import farticle.domain.entity.WorkSpace;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 博客的一些全局设置
 * 简单化设计，备份目录和不需要备份目录提前规划好，避免备份过滤文件
 * Linux根目录：/export/
 * window目录:D:/
 * *************目录说明，以Linux/export举例,用户名以qryc举例
 * /export/flyData----------------Fly网盘根目录
 * /export/flyData/users/qryc----------------------------------------------用户网盘根目录
 * /export/flyData/users/qryc/backData----------------------------------备份本目录
 * /export/flyData/users/qryc/backData/user/user_xxxx.gwf -------------------用户信息
 * /export/flyData/users/qryc/backData/articles------------------------用户文章系统根目录
 * /export/flyData/users/qryc/backData/articles/orgBlog_1.gwf ----------文章组织
 * /export/flyData/users/qryc/backData/articles/blog_1.gwf.gwf----------- 文章内容
 * /export/flyData/users/qryc/backData/pic/-------------------------------- 图片目录用户可自定义，如果存在当前目录，文章以/pic/引用图片
 * /export/flyData/users/qryc/zipData/----------------------------- zip back目录
 * /export/flyData/users/qryc/pic-pub/------------------------------------ 不需要备份的用户目录
 * 查看文件路径相对于 /export/flyData/qryc/.systemDat/pic/ &&/export/flyData/qryc/pic-pub/
 * <p>
 * <p>
 * /export/flyData/admin-----------------------------------------------------管理员目录
 * /export/flyData/admin/flyConfig.json -------------------------------------网站配置文件
 * ****************临时文件说明
 * /export/flyDataTemp -----------------------------------------------------临时目录 用来存放临时文件
 *
 * @author qryc 2020年03月20日，之前代码定位于博客代码，后续方向改为网盘为主，博客只是可以使用网盘内文件系统的一个模块
 * @author qryc
 */
public class FlyConfig {
    public static boolean onLine = true;
    public static String profile;
    //用户密码，默认用户备份文件压缩ZIP密码
    public static String defaultUserZipPwd;
    private static Map<String, WorkSpace> workSpaceMap;

    public FlyConfig(String profile, boolean onLine, String defaultUserZipPwd, List<WorkSpace> workSpaceList) {
        FlyConfig.profile = profile;
        FlyConfig.onLine = onLine;
        FlyConfig.defaultUserZipPwd = defaultUserZipPwd;

        workSpaceMap = new HashMap<>();
        workSpaceList.forEach(workSpace -> {
            workSpaceMap.put(workSpace.getName(), workSpace);
        });
    }

    public static WorkSpace getWorkSpace(String workspace) {
        return workSpaceMap.get(workspace);
    }


}
