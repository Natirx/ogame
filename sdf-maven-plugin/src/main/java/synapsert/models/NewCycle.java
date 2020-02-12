package synapsert.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;

@Builder
@Getter
@Value
public class NewCycle {
    String name;
    String environment;
    @JsonProperty("build")
    String buildName;
    String plannedStartDate;
    String plannedEndDate;
}