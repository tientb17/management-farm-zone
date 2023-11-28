package management_farm.request;

import lombok.Getter;
import lombok.Setter;

/**
 * Date: 11/27/2023
 * Project: Management-Farm
 * Author: ManhTien
 */
@Setter
@Getter
public class ForgotPasswordRequest {
    private String userLogin;
    private String email;
}
