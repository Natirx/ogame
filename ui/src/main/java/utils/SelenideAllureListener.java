package utils;

import com.codeborne.selenide.logevents.LogEvent;
import com.codeborne.selenide.logevents.LogEventListener;
import data.SystemProperties;
import engine.DriverUtils;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureLifecycle;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.StatusDetails;
import io.qameta.allure.model.StepResult;
import io.qameta.allure.util.ResultsUtils;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class SelenideAllureListener implements LogEventListener {

    private final AllureLifecycle lifecycle;

    public SelenideAllureListener() {
        this(Allure.getLifecycle());
    }

    public SelenideAllureListener(final AllureLifecycle lifecycle) {
        this.lifecycle = lifecycle;
    }

    @Override
    public void onEvent(final LogEvent event) {
        if (SystemProperties.SCREENSHOT_ON_EVERY_STEP) {
            lifecycle.getCurrentTestCase().ifPresent(uuid -> {
                List<String> actions = Arrays.asList("click", "set value");
                if (actions.stream().anyMatch(a -> event.toString().contains(a))) {
                    final String stepUUID = UUID.randomUUID().toString();

                    String name = event.toString();
                    /*
                     *  Replace password values with '*' character
                     * */
                    if (event.getElement().contains("password") && event.getSubject().startsWith("set value")) {
                        String s = event.getSubject().split("set value\\(")[1]
                                .replace(")", "")
                                .replaceAll(".", "*");
                        name = "$(" + event.getElement() + ") " + "set value(" + s + ")";
                    }

                    lifecycle.startStep(stepUUID, new StepResult()
                            .withName(name)
                            .withStatus(Status.PASSED));

                    lifecycle.addAttachment("Screenshot", "image/png", "png", DriverUtils.getScreenshot());

                    lifecycle.updateStep(stepResult -> stepResult.setStart(stepResult.getStart() - event.getDuration()));

                    if (LogEvent.EventStatus.FAIL.equals(event.getStatus())) {
                        lifecycle.updateStep(stepResult -> {
                            final StatusDetails details = ResultsUtils.getStatusDetails(event.getError())
                                    .orElse(new StatusDetails());
                            stepResult.setStatus(ResultsUtils.getStatus(event.getError()).orElse(Status.BROKEN));
                            stepResult.setStatusDetails(details);
                        });
                    }
                    lifecycle.stopStep(stepUUID);
                }
            });
        }
    }
}