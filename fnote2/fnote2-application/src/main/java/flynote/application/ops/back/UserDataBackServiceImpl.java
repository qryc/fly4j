package flynote.application.ops.back;

import backtool.service.zip.DirZipService;
import fly4j.common.domain.FlyResult;
import fly4j.common.file.FileAndDirPredicate;
import fly4j.common.file.FileInfoUtil;
import fly4j.common.mail.MailUtil2;
import fly4j.common.os.OsUtil;
import fly4j.common.util.DateUtil;
import fly4j.common.util.RepositoryException;
import fnote.common.DomainPathService;
import fnote.domain.config.FlyConfig;
import fnote.domain.config.LogConst;
import fnote.user.domain.entity.UserInfo;
import fnote.user.domain.infrastructure.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

/**
 * 备份服务
 *
 * @author xxgw ssss
 */
public class UserDataBackServiceImpl implements UserDataBackService {
    private UserRepository userRepository;
    private FileAndDirPredicate fileAndDirPredicate;
    private MailUtil2 mailUtil2;
    private DomainPathService pathService;

    @Override
    public void backSiteAndSendEmail(final String pin) throws IOException, RepositoryException {
        if (StringUtils.isBlank(pin)) {
            return;
        }
        //备份用户下的备份数据目录
        var sourceDirPath = pathService.getUserDirPath(pin);
        //备份到用户下载Zip目录
        var destZipDirPath = pathService.getUTempRootPath(pin).resolve("zipData");
        //取得压缩密码
        var zipPwd = ((UserInfo) userRepository.getUserinfo(pin))
                .getExtMapValue(UserInfo.EXT_ZIPPWD);
        if (StringUtils.isBlank(zipPwd)) {
            zipPwd = FlyConfig.defaultUserZipPwd;
        }
        //生成文件名
        var destZipFilePath = Path.of(destZipDirPath.toString(), OsUtil.getSimpleOsName() + pin + DateUtil.getHourStr4Name(new Date()) + ".zip");

        var flyResult = DirZipService.zipDirWithVerify(sourceDirPath.toFile(), destZipFilePath.toFile(), zipPwd, fileAndDirPredicate);
        sendMail(destZipFilePath, pin, flyResult);
    }

    private void sendMail(Path zipFilePath, String pin, FlyResult flyResult) {
        final var fileInfo = zipFilePath.toFile();

        if (null != fileInfo) {
            //发送备份文件到邮箱
            try {
                var flyUserInfo = (UserInfo) userRepository.getUserinfo(pin);
                var emailAddr = flyUserInfo.getUserConfig().getEmail();
                String mailContent = "flyback" + fileInfo.getName() + flyResult.getMsg();
                mailUtil2.sendMail(fileInfo.getName() + "-" + flyResult.isSuccess(), mailContent, fileInfo.getAbsolutePath(), emailAddr);
            } catch (Exception e) {
                LoggerFactory.getLogger(LogConst.FILE_EXCEPTION).error("back site send mail exception,pin is " + pin, e);
            }


        }
    }


    /**
     * 生成压缩文件备份文件信息
     *
     * @return
     */
    @Override
    public BackupFilesInfo getBackupFilesInfo(String pin) {
        var backupFilesInfo = new BackupFilesInfo();

        // 查找最新下载
        var targetZipFilePath = pathService.getUserBackDirPath(pin);// 得到备份目标路径
        var zipFiles = targetZipFilePath.toFile().listFiles((dir, name) -> name.endsWith(".zip"));
        if (null != zipFiles) {
            backupFilesInfo.setDownFileNames(FileInfoUtil.getFileInfos(zipFiles, pathService.getUserDirPath(pin)));
            backupFilesInfo.sortDownFileNames();
        }


        return backupFilesInfo;
    }

    @Override
    public String getLastDownFilePath(String pin) {

        // 查找最新下载
        var targetZipFilePath = pathService.getUserBackDirPath(pin);// 得到备份目标路径
        var zipFilesArray = targetZipFilePath.toFile().listFiles((dir, name) -> name.endsWith(".zip"));
        var zipFiles = Arrays.asList(zipFilesArray);
        Collections.sort(zipFiles, (f1, f2) -> (int) (f2.lastModified() - f1.lastModified()));


        return zipFiles.get(0).getAbsolutePath();
    }


    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void setFileAndDirPredicate(FileAndDirPredicate fileAndDirPredicate) {
        this.fileAndDirPredicate = fileAndDirPredicate;
    }

    public void setMailUtil2(MailUtil2 mailUtil2) {
        this.mailUtil2 = mailUtil2;
    }

    public void setPathService(DomainPathService pathService) {
        this.pathService = pathService;
    }
}
