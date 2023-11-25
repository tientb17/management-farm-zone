package management_farm.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Date: 11/24/2023
 * Project: Management-Farm
 * Author: ManhTien
 */
@Getter
@Setter
@NoArgsConstructor
public class LoginRequest {
    private String userLogin;
    private String password;
}
