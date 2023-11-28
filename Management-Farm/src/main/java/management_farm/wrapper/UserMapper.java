package management_farm.wrapper;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Date: 11/27/2023
 * Project: Management-Farm
 * Author: ManhTien
 */

@Setter
@Getter
public class UserMapper {
    private Long id;
    private String address;
    private Date birthDate;
    private Date createDate;
    private String email;
    private String fullName;
    private String phone;
    private String status;
    private String userLogin;
    private Date updateDate;
    private String updatedBy;
    private String roleName;

    public UserMapper(Long id, String address, Date birthDate, Date createDate, String email, String fullName, String phone, String status, String userLogin, Date updateDate, String updatedBy, String roleName) {
        this.id = id;
        this.address = address;
        this.birthDate = birthDate;
        this.createDate = createDate;
        this.email = email;
        this.fullName = fullName;
        this.phone = phone;
        this.status = status;
        this.userLogin = userLogin;
        this.updateDate = updateDate;
        this.updatedBy = updatedBy;
        this.roleName = roleName;
    }
}
