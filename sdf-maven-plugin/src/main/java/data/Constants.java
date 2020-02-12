package data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import static java.lang.System.getProperty;

public interface Constants {
    String PROJECT_KEY = "ELEM";
    String AWS_REGION = System.getProperty("aws.region");
    String AWS_ACCESS_KEY = System.getProperty("aws.access.key");
    String AWS_SECRET_KEY = System.getProperty("aws.secret.key");
    String AWS_BUCKET_NAME = System.getProperty("aws.bucket.name");
    Path REPORT_ARCHIVE_FILE_PATH = Path.of("", "target/report.zip");
    Path AWS_REPORT_URL_FILE_PATH = Path.of("", "target/aws_report_url.txt");
    String SPRINT_NUMBER = System.getProperty("sprint.number");
    String JIRA_USERNAME = getProperty("jira.user");
    String JIRA_PASS = getProperty("jira.pass");
    String JIRA_BASE_URL = getProperty("jira.host");
    String EMAIL_ACCOUNT = System.getProperty("email.account");
    String EMAIL_PASSWORD = System.getProperty("email.password");
    String EMAIL_RECIPIENTS = System.getProperty("email.recipients");
    String GITLAB_REPORT_LINK = System.getProperty("gitlab.report.url");
    String GITLAB_PIPELINE_LINK = System.getProperty("gitlab.pipeline.url");
    String EMAIL_PASSWORD_OF_STANDARD_USER = System.getProperty("email.password.standard.user");

    static String getAwsUrl() {
        Stream<String> lines = null;
        try {
            lines = Files.lines(AWS_REPORT_URL_FILE_PATH);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines == null ? "" : lines.findFirst().orElse("");
    }
}
