package management_farm.model;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

/**
 * Date: 11/24/2023
 * Project: Management-Farm
 * Author: ManhTien
 */

@Data
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "forgot_password")
public class ForgotPassword {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "forgot_password_id")
    private Long fgPasswordId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_fk", nullable = false)
    private User userId;

    @Column(name = "reset_token")
    private String resetToken;

    @Column(name = "first_date_forgot")
    private Date firstDateForgot;

    @Column(name = "last_date_reset")
    private Date lastDateReset;
}
