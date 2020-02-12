package mail;

import lombok.Builder;
import lombok.Getter;
import lombok.Value;

@Builder
@Getter
@Value
public class EmailAccountModel {
    private final String username;
    private final String password;
}
