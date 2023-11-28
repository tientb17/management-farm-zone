package management_farm.request;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Date: 11/25/2023
 * Project: Management-Farm
 * Author: ManhTien
 */
@Setter
@Getter
public class SignupRequest {

    private String fullName;
    private String address;
    private String birthDate;
    private String email;
    private String phone;
    private String userLogin;
    private String password;
}
