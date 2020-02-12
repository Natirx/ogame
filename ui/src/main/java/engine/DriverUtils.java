package engine;

import com.codeborne.selenide.WebDriverRunner;
import lombok.extern.java.Log;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriverException;

@Log
public final class DriverUtils {

    private static final String SESSION_TIMED_OUT_OR_NOT_FOUND = "Session timed out or not found";

    public static void stop() {
        log.info(String.format("Trying to close WebDriver in thread [%s]", getCurrentThreadId()));
        try {
            WebDriverRunner.closeWebDriver();
            log.info(String.format("Closed WebDriver in thread [%s]", getCurrentThreadId()));
        } catch (WebDriverException e) {
            if (e.getMessage().contains(SESSION_TIMED_OUT_OR_NOT_FOUND)) {
                log.info(String.format("Was not able to close WebDriver in thread [%s] because [%s]",
                        getCurrentThreadId(), SESSION_TIMED_OUT_OR_NOT_FOUND));
            } else {
                throw e;
            }
        }
    }

    private static long getCurrentThreadId() {
        return Thread.currentThread().getId();
    }

    public static byte[] getScreenshot() {
        log.info(String.format("Trying to get screenshot in thread [%s]", getCurrentThreadId()));
        if (WebDriverRunner.hasWebDriverStarted()) {
            log.info(String.format("WebDriver is opened in thread [%s], trying to get screenshot", getCurrentThreadId()));
            byte[] screenshotBytes;
            try {
                screenshotBytes = ((TakesScreenshot) WebDriverRunner.getWebDriver()).getScreenshotAs(OutputType.BYTES);
            } catch (WebDriverException e) {
                if (e.getMessage().contains(SESSION_TIMED_OUT_OR_NOT_FOUND)) {
                    log.info(String.format("Was not able to get screenshot in thread [%s] because [%s]",
                            getCurrentThreadId(), SESSION_TIMED_OUT_OR_NOT_FOUND));
                    screenshotBytes = SESSION_TIMED_OUT_OR_NOT_FOUND.getBytes();
                } else {
                    throw e;
                }
            }
            log.info(String.format("Successfully got screenshot in thread [%s]", getCurrentThreadId()));
            return screenshotBytes;
        } else {
            log.info(String.format("WebDriver is not opened in thread [%s], returning empty screenshot", getCurrentThreadId()));
            return new byte[]{};
        }
    }
}
