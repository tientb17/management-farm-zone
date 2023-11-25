package management_farm.dao;

import management_farm.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

/**
 * Date: 11/24/2023
 * Project: Management-Farm
 * Author: ManhTien
 */
public interface UserDao extends JpaRepository<User, Long> {
    User findUserByUserLogin(@Param("user_login") String userLogin);
}
