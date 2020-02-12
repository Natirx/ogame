package mail;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class MailConnectivityBuilder {
    private static final String SYS_PROP_NAME = "mail.store.protocol";
    private static final String SYS_PROP_VALUE = "imaps";
    private static final String GMAIL_PROTOCOL = "imap.gmail.com";

    public static MailConnectivityModel build(EmailAccountModel account) {
        return new MailConnectivityModel.MailConnectivityModelBuilder()
                .protocolSysName(SYS_PROP_NAME)
                .protocolSysValue(SYS_PROP_VALUE)
                .protocol(getProtocol())
                .emailAccount(account)
                .folderNames(getFolderNames())
                .sendingProperties(getSendingProps())
                .build();
    }

    private static Properties getSendingProps() {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        return props;
    }

    private static List<String> getFolderNames() {
        List<String> folders = new ArrayList<>();
        folders.add("[Gmail]/All Mail");
        folders.add("[Gmail]/Spam");
        return folders;
    }

    private static String getProtocol() {
        return GMAIL_PROTOCOL;
    }
}
