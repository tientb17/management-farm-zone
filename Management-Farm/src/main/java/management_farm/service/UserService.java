package management_farm.service;

import management_farm.request.*;
import management_farm.response.CustomerResponse;
import management_farm.wrapper.UserMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Date: 11/24/2023
 * Project: Management-Farm
 * Author: ManhTien
 */
public interface UserService {
    ResponseEntity<CustomerResponse<String>> login(LoginRequest loginRequest);

    ResponseEntity<CustomerResponse<List<UserMapper>>> getAllUser();

    ResponseEntity<CustomerResponse<String>> signup(SignupRequest signupRequest);

    ResponseEntity<CustomerResponse<String>> updateAvatar(MultipartFile file, Long id);

    ResponseEntity<byte[]> getAvatarById(Long id);

    ResponseEntity<CustomerResponse<String>> updateStatus(Long id, UpdateStatusUserRequest status);

    ResponseEntity<CustomerResponse<String>> forgotPassword(ForgotPasswordRequest forgotPasswordRequest);

    ResponseEntity<CustomerResponse<String>> resetPassword(String resetToken, ResetPasswordRequest resetPasswordRequest);
}
