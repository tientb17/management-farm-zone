package management_farm.dao;

import management_farm.model.User;
import management_farm.request.UpdateStatusUserRequest;
import management_farm.wrapper.UserMapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Date: 11/24/2023
 * Project: Management-Farm
 * Author: ManhTien
 */
public interface UserDao extends JpaRepository<User, Long> {
    User findUserByUserLogin(@Param("user_login") String userLogin);

    @Transactional
    @Modifying
    Integer updateAvatar(@Param("avatar") byte[] bytes,
                         @Param("id") Long id,
                         @Param("updateDate") Date updateDate,
                         @Param("updatedBy") String updatedBy);

    List<UserMapper> getAllUser();

    @Transactional
    @Modifying
    Integer updateStatus(@Param("id") Long id,
                         @Param("status") String status,
                         @Param("updateDate") Date date,
                         @Param("updatedBy")String currentUser);

    User findByUserLoginAndEmail(@Param("userLogin") String userLogin, @Param("email") String email);

    @Transactional
    @Modifying
    Integer resetPassword(@Param("id")Long userId,
                          @Param("newPassword") String newPassword,
                          @Param("updateDate")Date date);

    List<String> getAllUserAdminRole();
}
