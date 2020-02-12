package synapsert;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public final class TestStatusCollector {
    private final static ConcurrentMap<String, TestExecutionStatus> TEST_STATUS_MAP = new ConcurrentHashMap<>();
    private final static String FILE_PATH = "target/test_status.txt";
    private final static String KEY_VALUE_SEPARATOR = ",";

    public static void put(String testId, TestExecutionStatus status) {
        TEST_STATUS_MAP.merge(testId, status, (v1, v2) ->
                (v2.equals(TestExecutionStatus.Failed)) ? v2 : v1);
    }

    public static void serialize() {
        try {
            Files.write(Path.of(System.getProperty("user.dir"), FILE_PATH), () -> TEST_STATUS_MAP.entrySet().stream()
                    .<CharSequence>map(e -> e.getKey() + KEY_VALUE_SEPARATOR + e.getValue().name()).iterator());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Map<String, String> deserialize(String dirPath) {
        Map<String, String> map = new HashMap<>();
        try {
            Files.lines(Path.of(dirPath, FILE_PATH)).forEach(e -> {
                String[] split = e.split(KEY_VALUE_SEPARATOR);
                map.put(split[0], split[1]);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }
}
