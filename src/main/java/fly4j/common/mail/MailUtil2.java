package fly4j.common.mail;

import org.apache.commons.mail.EmailException;

/**
 * 非静态
 * Created by qryc on 2022/02/07.
 */
public class MailUtil2 {
    private MailUtil.MailConfigModel mailConfigModel;

    public void sendMail(String subject, String content, String filename, String mailTo) throws EmailException {
        MailUtil.sendMail(mailConfigModel, subject, content, filename, mailTo);
    }

    public void sendSimpleMail(String subject, String content, String mailTo) throws EmailException {
        MailUtil.sendSimpleMail(mailConfigModel, subject, content, mailTo);
    }

    public void setMailConfigModel(MailUtil.MailConfigModel mailConfigModel) {
        this.mailConfigModel = mailConfigModel;
    }
}
