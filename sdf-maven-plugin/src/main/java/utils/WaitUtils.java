package utils;

import java.time.Duration;

public final class WaitUtils {
    public static void until(Duration duration, Duration polling, Runnable runnable) {
        long end = laterBy(duration.toMillis());
        while (true) {
            try {
                runnable.run();
                return;
            } catch (AssertionError | RuntimeException e) {
                if (!isNowBefore(end)) {
                    throw new RuntimeException("Timeout exception: " + e.getMessage());
                }
            }
            try {
                Thread.sleep(polling.toMillis());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private static long laterBy(long durationInMillis) {
        return System.currentTimeMillis() + durationInMillis;
    }

    private static boolean isNowBefore(long endInMillis) {
        return System.currentTimeMillis() < endInMillis;
    }
}
