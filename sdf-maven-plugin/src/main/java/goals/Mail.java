package goals;

import data.Constants;
import lombok.extern.java.Log;
import mail.EmailAccountModel;
import mail.MailUtil;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.util.List;

import static data.Constants.GITLAB_PIPELINE_LINK;
import static data.Constants.GITLAB_REPORT_LINK;

@Log
@Mojo(name = "mail", defaultPhase = LifecyclePhase.COMPILE)
public class Mail extends AbstractMojo {

    @Parameter(property = "skip", defaultValue = "false")
    private boolean skip;

    @Override
    public void execute() {
        if (!skip) {
            if (Constants.EMAIL_ACCOUNT == null || Constants.EMAIL_PASSWORD == null || Constants.EMAIL_RECIPIENTS == null) {
                throw new IllegalArgumentException("Email account/password/recipients must not be null");
            }
            EmailAccountModel accountModel = EmailAccountModel.builder()
                    .username(Constants.EMAIL_ACCOUNT)
                    .password(Constants.EMAIL_PASSWORD)
                    .build();
            log.info("Sending email with gitlab report url....");
            new MailUtil(accountModel).send(List.of(Constants.EMAIL_RECIPIENTS.split(",")),
                    "Report of execution UI test cases of Case Submission project",
                    "Please find the report by following the next url: " + GITLAB_REPORT_LINK + "\n" +
                            "To publish/deploy result please go to the next url and trigger appropriate job: " + GITLAB_PIPELINE_LINK);
            log.info("Report has been sent");
        }
    }
}