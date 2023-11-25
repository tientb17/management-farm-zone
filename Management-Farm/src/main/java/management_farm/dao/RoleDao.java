package management_farm.dao;

import management_farm.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

/**
 * Date: 11/24/2023
 * Project: Management-Farm
 * Author: ManhTien
 */
public interface RoleDao extends JpaRepository<Role, Long> {
    Role findByRoleName(@Param("roleName") String roleName);
}
