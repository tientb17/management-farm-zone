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

@NamedQuery(name = "Role.findByRoleName",
        query = "select r from Role r where r.roleName = :roleName")

@Data
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long roleId;

    @Column(name = "name")
    private String roleName;

    private String status;

    @Column(name = "create_date")
    private Date createDate;

    @Column(name = "created_by")
    private String createdBy;
}
