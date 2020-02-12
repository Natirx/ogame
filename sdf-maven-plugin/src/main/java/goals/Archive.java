package goals;

import data.Constants;
import lombok.extern.java.Log;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.zeroturnaround.zip.ZipUtil;

import java.nio.file.Path;

@Log
@Mojo(name = "archive", defaultPhase = LifecyclePhase.COMPILE)
public class Archive extends AbstractMojo {

    @Parameter(property = "skip", defaultValue = "false")
    private boolean skip;
    @Parameter(property = "reportPath")
    private String reportPath;

    @Override
    public void execute() {
        if (!skip) {
            if (reportPath == null) {
                throw new IllegalArgumentException("Report path must be provided");
            }
            log.info("Packing report....");
            ZipUtil.pack(Path.of("", reportPath).toFile(), Constants.REPORT_ARCHIVE_FILE_PATH.toFile());
            log.info("Report has been packed");
        }
    }
}