package management_farm.model;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NamedQuery;

import javax.persistence.*;
import java.sql.Blob;
import java.util.Date;

/**
 * Date: 11/24/2023
 * Project: Management-Farm
 * Author: ManhTien
 */

@NamedQuery(name = "User.findUserByUserLogin",
        query = "select u from User u where u.userLogin =:user_login")
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

    @Column(name = "update_date")
    private Date updateDate;

    @Column(name = "avatar")
    @Lob
    private byte[] avatar;
}
