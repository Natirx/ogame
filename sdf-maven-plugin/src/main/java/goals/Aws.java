package goals;

import com.amazonaws.HttpMethod;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.Upload;
import lombok.extern.java.Log;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

import static data.Constants.*;

@Log
@Mojo(name = "aws", defaultPhase = LifecyclePhase.COMPILE)
public class Aws extends AbstractMojo {

    @Parameter(property = "skip", defaultValue = "false")
    private boolean skip;

    @Override
    public void execute() {
        if (!skip) {
            String keyName = "report_" + LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
            BasicAWSCredentials credentials = new BasicAWSCredentials(AWS_ACCESS_KEY, AWS_SECRET_KEY);
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(credentials))
                    .withRegion(AWS_REGION)
                    .withForceGlobalBucketAccessEnabled(true)
                    .build();
            TransferManager tm = TransferManagerBuilder.standard()
                    .withS3Client(s3Client)
                    .build();
            log.info("Uploading report to AWS...");
            Upload upload = tm.upload(AWS_BUCKET_NAME, keyName, REPORT_ARCHIVE_FILE_PATH.toFile());
            try {
                upload.waitForUploadResult();
            } catch (AmazonS3Exception | InterruptedException e) {
                e.printStackTrace();
            }
            log.fine("Report has been uploaded to AWS");
            GeneratePresignedUrlRequest generatePresignedUrlRequest =
                    new GeneratePresignedUrlRequest(AWS_BUCKET_NAME, keyName).withMethod(HttpMethod.GET);
            log.info("Getting url of uploaded report...");
            URL url = s3Client.generatePresignedUrl(generatePresignedUrlRequest);
            log.info("Writing url into file...");
            try {
                Files.write(AWS_REPORT_URL_FILE_PATH, () -> Stream.of(url.toString()).<CharSequence>map(e -> e).iterator());
            } catch (IOException e) {
                e.printStackTrace();
            }
            log.fine("AWS report url has been saved into file");
        }
    }
}
