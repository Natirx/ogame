package data;

import static java.lang.System.getProperty;

public interface SystemProperties {
    String OG_URL = getProperty("og.url");
    String OG_USERNAME = getProperty("og.user");
    String OG_PASS = getProperty("og.pass");
    String DRIVER_TYPE = getProperty("driver.type");
    String REMOTE_DRIVER_TYPE = getProperty("remote.driver.type");
    String REMOTE_URL = getProperty("remote.url");
    boolean SCREENSHOT_ON_EVERY_STEP = Boolean.parseBoolean(getProperty("screenshot_on_every_step"));
    String PARALLEL_MODE = getProperty("parallel.mode");
    String THREAD_COUNT = getProperty("thread.count");
}
