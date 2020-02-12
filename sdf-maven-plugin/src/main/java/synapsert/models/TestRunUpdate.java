package synapsert.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;

@Builder
@Getter
@Value
public class TestRunUpdate {

    @JsonProperty("testcaseKey")
    String testCaseKey;
    String result;
    String comment;
}
