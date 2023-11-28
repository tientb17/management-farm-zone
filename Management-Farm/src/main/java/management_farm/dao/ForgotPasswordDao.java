package management_farm.dao;

import management_farm.model.ForgotPassword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

/**
 * Date: 11/27/2023
 * Project: Management-Farm
 * Author: ManhTien
 */
public interface ForgotPasswordDao extends JpaRepository<ForgotPassword, Long> {
    ForgotPassword findByUserId(@Param("userId") Long userId);
    ForgotPassword findByResetToken(@Param("resetToken") String resetToken);

}
