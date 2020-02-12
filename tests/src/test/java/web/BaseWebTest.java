package web;

import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.logevents.SelenideLogger;
import engine.DriverBinaryManager;
import engine.DriverConfigurator;
import engine.DriverUtils;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestContext;
import org.testng.annotations.*;
import org.testng.xml.XmlSuite;
import synapsert.TestStatusCollector;
import utils.SelenideAllureListener;
import utils.TestListener;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static data.SystemProperties.*;
import static java.lang.System.getProperties;

@Listeners(TestListener.class)
public class BaseWebTest {
    private final static ConcurrentMap<String, Object> ENV_PROPERTIES = new ConcurrentHashMap<>();

    @BeforeSuite
    protected void beforeSuite(ITestContext context) {
        DriverBinaryManager.setupWebDriverBinary();//download web driver binary
        if (PARALLEL_MODE != null && THREAD_COUNT != null) {
            context.getSuite().getXmlSuite().setParallel(XmlSuite.ParallelMode.getValidParallel(PARALLEL_MODE));
            context.getSuite().getXmlSuite().setThreadCount(Integer.parseInt(THREAD_COUNT));
        }
    }

    @BeforeMethod(description = "Initialize web driver configuration")
    protected void beforeMethod() {
        DriverConfigurator.configure();//this method doesn't open the browser yet
        if (SCREENSHOT_ON_EVERY_STEP) {
            SelenideLogger.addListener("Selenide Allure Listener", new SelenideAllureListener());
        }
    }

    @AfterMethod(alwaysRun = true, description = "Close driver")
    protected void tearDown() {
        if (ENV_PROPERTIES.isEmpty()) {
            ENV_PROPERTIES.put("java_version", getProperties().getProperty("java.vm.version"));
            ENV_PROPERTIES.put("os_name", getProperties().getProperty("os.name"));
            ENV_PROPERTIES.put("gitlab_user_email", getProperties().getProperty("gitlab_user_email", "Gitlab User Email not found"));
            ENV_PROPERTIES.put("gitlab_user_name", getProperties().getProperty("gitlab_user_name", "Gitlab User Name not found"));
            ENV_PROPERTIES.putAll(((RemoteWebDriver) WebDriverRunner.getWebDriver()).getCapabilities().asMap());
        }
        System.out.println(LocalDateTime.now());
        DriverUtils.stop();
    }

    @AfterSuite(alwaysRun = true)
    protected void afterSuite() {
        TestStatusCollector.serialize();
        Path path = Path.of("", "target/allure-results/environment.properties");
        System.out.println(path.toAbsolutePath().toString());
        try {
            Files.write(path, () -> ENV_PROPERTIES.entrySet().stream().<CharSequence>map(e -> e.getKey() + "=" + e.getValue()).iterator());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
