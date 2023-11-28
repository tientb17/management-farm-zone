package management_farm.restImpl;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import management_farm.request.*;
import management_farm.response.CustomerResponse;
import management_farm.rest.UserRest;
import management_farm.service.UserService;
import management_farm.wrapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Date: 11/24/2023
 * Project: Management-Farm
 * Author: ManhTien
 */
@RestController
public class UserRestImpl implements UserRest {
    @Autowired
    private UserService userService;

    @Override
    @ApiOperation(value = "Login")
    public ResponseEntity<CustomerResponse<String>> login(LoginRequest loginRequest) {
        return userService.login(loginRequest);
    }

    @Override
    @ApiOperation(value = "Signup")
    public ResponseEntity<CustomerResponse<String>> signup(SignupRequest signupRequest) {
        return userService.signup(signupRequest);
    }

    @Override
    @ApiOperation(value = "Update Avatar", authorizations = { @Authorization(value="Bearer") })
    public ResponseEntity<CustomerResponse<String>> updateAvatar(MultipartFile file, Long id) {
        return userService.updateAvatar(file, id);
    }

    @Override
    @ApiOperation(value = "Get All User", authorizations = { @Authorization(value="Bearer") })
    public ResponseEntity<CustomerResponse<List<UserMapper>>> getAllUser() {
        return userService.getAllUser();
    }

    @Override
    @ApiOperation(value = "Get Avatar User", authorizations = { @Authorization(value="Bearer") })
    public ResponseEntity<byte[]> getAvatarById(Long id) {
        return userService.getAvatarById(id);
    }

    @Override
    @ApiOperation(value = "Update Status User", authorizations = { @Authorization(value="Bearer") })
    public ResponseEntity<CustomerResponse<String>> updateStatusUser(Long id, UpdateStatusUserRequest status) {
        return userService.updateStatus(id, status);
    }

    @Override
    @ApiOperation(value = "Forgot Password")
    public ResponseEntity<CustomerResponse<String>> forgotPassword(ForgotPasswordRequest forgotPasswordRequest) {
        return userService.forgotPassword(forgotPasswordRequest);
    }

    @Override
    @ApiOperation(value = "Reset Password")
    public ResponseEntity<CustomerResponse<String>> resetPassword(String resetToken, ResetPasswordRequest resetPasswordRequest) {
        return userService.resetPassword(resetToken, resetPasswordRequest);
    }
}
