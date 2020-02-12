package utils;

import io.qameta.allure.TmsLink;
import io.qameta.allure.TmsLinks;
import lombok.extern.java.Log;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Log
public class TestListener implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {

    }

    @Override
    public void onTestSuccess(ITestResult result) {
    }

    @Override
    public void onTestFailure(ITestResult result) {
        AllureUtils.takeScreenshot(result.getMethod().getMethodName());
    }

    @Override
    public void onTestSkipped(ITestResult result) {

    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {

    }

    @Override
    public void onStart(ITestContext context) {

    }

    @Override
    public void onFinish(ITestContext context) {

    }

    private List<String> getTestCaseIds(ITestResult iTestResult) {
        List<String> result = new ArrayList<>();
        Annotation[] annotations = iTestResult.getMethod().getConstructorOrMethod().getMethod().getDeclaredAnnotations();
        Optional<Annotation> optional = Arrays.stream(annotations).filter(a -> a instanceof TmsLink).findFirst();
        if (optional.isPresent()) {
            result.add(((TmsLink) optional.get()).value());
        } else {
            optional = Arrays.stream(annotations).filter(a -> a instanceof TmsLinks).findFirst();
            if (optional.isPresent()) {
                TmsLinks tmsLinks = (TmsLinks) optional.get();
                if (tmsLinks.value().length == 0) {
                    log.warning("'" + iTestResult.getMethod().getConstructorOrMethod().getMethod().getName() + "' " +
                            "test method in '" + iTestResult.getTestClass().getRealClass().getName() + "' class " +
                            "does not contain the annotation '" + TmsLink.class.getName() + "' inside '" + TmsLinks.class.getName() + "'");
                } else {
                    Arrays.stream(tmsLinks.value()).forEach(tms -> result.add(tms.value()));
                }
            } else {
                log.warning("'" + iTestResult.getMethod().getConstructorOrMethod().getMethod().getName() + "' " +
                        "test method in '" + iTestResult.getTestClass().getRealClass().getName() + "' class " +
                        "does not contain the annotation '" + TmsLink.class.getName() + "' or '" + TmsLinks.class.getName() + "'");
            }
        }
        return result;
    }
}
