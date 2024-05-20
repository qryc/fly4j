package fly4j.common.mail;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Objects;

/**
 * smtp 发送邮件 端口25 sll加密端口994/465
 * pop3 接收邮件 端口:110  SLL加密类型端口：995
 * imap 接收邮件 网易邮箱的端口是：143  SLL加密类型端口：993
 * Created by qiuriyuchen on 2015/12/4.
 */
public class MailUtil {
    static Logger log = LoggerFactory.getLogger(MailUtil.class);

    public static void sendMail(MailConfigModel mailConfigModel, String subject, String content, String filename, String mailTo) throws EmailException {
        sendMailInner(mailConfigModel, subject, content, new File(filename), mailTo);
    }

    public static void sendSimpleMail(MailConfigModel mailConfigModel, String subject, String content, String mailTo) throws EmailException {

        sendMailInner(mailConfigModel, subject, content, null, mailTo);
    }

    public static void main(String[] args) throws Exception {
        var subject = "iknow-testss";
        var content = "iknow-testss";
        var mailTo = "qiuriyuchen@aliyun.com";

//        sendSimpleMail(subject, content, "README.md", mailTo);
//        sendMailInner(subject, content, new File("README.md"), mailTo);
        var sysEmailConfig = new MailConfigModel();
        sendMailInner(sysEmailConfig, subject, content, null, mailTo);
        File directory = new File("");//设定为当前文件夹

        System.out.println(directory.getCanonicalPath());//获取标准的路径
        System.out.println(directory.getAbsolutePath());//获取绝对路径

    }

    private static void sendMailInner(MailConfigModel mailConfigModel, String subject, String content, File file, String mailTo) throws EmailException {
        Objects.requireNonNull(mailConfigModel, "sysEmailConfig is null,please init it before use");
        var email = new MultiPartEmail();
        email.setHostName(mailConfigModel.getHostName());
        email.setSmtpPort(465);
        email.setAuthenticator(new DefaultAuthenticator(mailConfigModel.getUserName(), mailConfigModel.getPassword()));
        email.setSSLOnConnect(true);
        email.setFrom(mailConfigModel.getFromEmail());
        email.setSubject(subject);
        email.setMsg(content);
        email.addTo(mailTo);
        if (null != file) {
            // Create the attachment
            EmailAttachment attachment = new EmailAttachment();
            attachment.setPath(file.getAbsolutePath());
            attachment.setDisposition(EmailAttachment.ATTACHMENT);
            attachment.setDescription("附件");
            attachment.setName(file.getName());
            // add the attachment
            email.attach(attachment);
        }
        email.send();
        log.info("Email send success");
    }

    /**
     * Created by qryc on 2020/5/3.
     */
    public static class MailConfigModel {
        private String hostName;
        private String userName;
        private String password;
        private String fromEmail;

        public String getHostName() {
            return hostName;
        }

        public MailConfigModel setHostName(String hostName) {
            this.hostName = hostName;
            return this;
        }

        public String getUserName() {
            return userName;
        }

        public MailConfigModel setUserName(String userName) {
            this.userName = userName;
            return this;
        }

        public String getPassword() {
            return password;
        }

        public MailConfigModel setPassword(String password) {
            this.password = password;
            return this;
        }

        public String getFromEmail() {
            return fromEmail;
        }

        public MailConfigModel setFromEmail(String fromEmail) {
            this.fromEmail = fromEmail;
            return this;
        }
    }
}
