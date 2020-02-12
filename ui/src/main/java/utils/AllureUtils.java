package utils;

import engine.DriverUtils;
import io.qameta.allure.Attachment;

public final class AllureUtils {

    @Attachment("{name}")
    public static byte[] takeScreenshot(String name) {
        return DriverUtils.getScreenshot();
    }
}
