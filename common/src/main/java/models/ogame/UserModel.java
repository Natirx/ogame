package models.ogame;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@EqualsAndHashCode
public class UserModel {
    private String username;
    private String password;

    @Override
    public String toString() {
        return "UserModel{" +
                "username='" + username + '\'' +
                ", password='" + password.replaceAll(".", "*") + '\'' +
                '}';
    }
}
