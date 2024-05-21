package flynote.application.ops.back;

import fly4j.common.util.RepositoryException;

import java.io.IOException;

/**
 * 备份服务
 *
 * @author qryc
 */
public interface UserDataBackService {
    /**
     * 备份用户的博客内容
     * 生成压缩文件，并把压缩文件发到用户的邮箱
     *
     * @param pin
     */
    void backSiteAndSendEmail(String pin) throws IOException, RepositoryException;


    /**
     * 得到用户的备份文件列表
     */
    BackupFilesInfo getBackupFilesInfo(String pin);

    String getLastDownFilePath(String pin);


}
