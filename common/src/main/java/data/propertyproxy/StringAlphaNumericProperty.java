package data.propertyproxy;

import lombok.ToString;
import java.io.Serializable;
import static data.constans.Constants.*;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

@ToString
public class StringAlphaNumericProperty implements GeneratedProperty<String>, Serializable {
    private static final int CHAR_COUNT = 6;
    private static final long serialVersionUID = 4940551222715611063L;
    private String value;

    public StringAlphaNumericProperty(String testId) {
        this.value = new StringBuilder()
                .append(TEST_CASE_GLOBAL_PREFIX)
                .append("_")
                .append(testId)
                .append("_")
                .append(randomAlphanumeric(CHAR_COUNT))
                .toString();
    }

    public String getValue() {
        return value;
    }
}
