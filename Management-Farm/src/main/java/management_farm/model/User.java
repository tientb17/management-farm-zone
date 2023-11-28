package management_farm.model;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NamedQuery;

import javax.persistence.*;
import java.util.Date;

/**
 * Date: 11/24/2023
 * Project: Management-Farm
 * Author: ManhTien
 */

@NamedQuery(name = "User.findUserByUserLogin",
        query = "select u from User u where u.userLogin =:user_login")
@NamedQuery(name = "User.updateAvatar",
        query = "update User u set u.avatar =:avatar, u.updateDate =:updateDate, u.updatedBy =:updatedBy where u.id =:id")
@NamedQuery(name = "User.getAllUser",
        query = "select new management_farm.wrapper.UserMapper(u.id, u.address, u.birthDate, u.createDate, u.email, u.fullName, u.phone, u.status, u.userLogin, u.updateDate, u.updatedBy, r.roleName) from User u inner join Role r on u.roleId.id = r.id where u.userLogin != 'admin'")
@NamedQuery(name = "User.updateStatus",
        query = "update User u set u.status =:status, u.updateDate=:updateDate, u.updatedBy=:updatedBy where u.id =:id")
@NamedQuery(name = "User.findByUserLoginAndEmail",
        query = "select u from User u where u.userLogin =:userLogin and u.email =:email")
@NamedQuery(name = "User.resetPassword",
        query = "update User u set u.password =:newPassword, u.updateDate =:updateDate where u.id =:id")
@NamedQuery(name = "User.getAllUserAdminRole",
        query = "select u.email from User u where u.roleId.roleName = 'admin'")
@Data
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "birth_date")
    private Date birthDate;

    @Column(name = "address")
    private String address;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email")
    private String email;

    @Column(name = "user_login")
    private String userLogin;

    @Column(name = "password")
    private String password;

    @Column(name = "status")
    private String status;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_fk", nullable = false)
    private Role roleId;

    @Column(name = "create_date")
    private Date createDate;

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "update_date")
    private Date updateDate;

    @Column(name = "avatar")
    @Lob
    private byte[] avatar;
}
