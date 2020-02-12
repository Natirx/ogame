package mail;

import lombok.Builder;
import lombok.Getter;
import lombok.Value;

import java.util.List;
import java.util.Properties;

@Builder
@Getter
@Value
public class MailConnectivityModel {
    private final EmailAccountModel emailAccount;
    private final String protocol;
    private final String protocolSysName;
    private final String protocolSysValue;
    private final Properties sendingProperties;
    private final List<String> folderNames;
}
