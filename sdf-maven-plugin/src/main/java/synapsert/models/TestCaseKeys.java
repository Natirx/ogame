package synapsert.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Value;

import java.util.List;

@Builder
@Getter
@Value
public class TestCaseKeys {
    List<String> testCaseKeys;
}
