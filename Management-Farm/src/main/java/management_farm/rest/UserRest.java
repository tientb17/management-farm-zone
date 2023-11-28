package management_farm.rest;

import management_farm.request.*;
import management_farm.response.CustomerResponse;
import management_farm.wrapper.UserMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Date: 11/24/2023
 * Project: Management-Farm
 * Author: ManhTien
 */
@RequestMapping("/users")
public interface UserRest {
    @PostMapping("/login")
    ResponseEntity<CustomerResponse<String>> login(@RequestBody LoginRequest loginRequest);

    @PostMapping("/signup")
    ResponseEntity<CustomerResponse<String>> signup(@RequestBody SignupRequest signupRequest);

    @PostMapping("/updateAvatar/{id}")
    ResponseEntity<CustomerResponse<String>> updateAvatar(@RequestParam("file") MultipartFile file, @PathVariable("id") Long id);
    @GetMapping("/getAllUser")
    ResponseEntity<CustomerResponse<List<UserMapper>>> getAllUser();

    @GetMapping("/getAvatar/{id}")
    ResponseEntity<byte[]> getAvatarById(@PathVariable Long id);

    @PostMapping("/updateStatus/{id}")
    ResponseEntity<CustomerResponse<String>> updateStatusUser(@PathVariable Long id, @RequestBody UpdateStatusUserRequest status);

    @PostMapping("/forgotPassword")
    ResponseEntity<CustomerResponse<String>> forgotPassword(@RequestBody ForgotPasswordRequest forgotPasswordRequest);

    @PostMapping("/resetPassword")
    ResponseEntity<CustomerResponse<String>> resetPassword(@RequestParam String resetToken, @RequestBody ResetPasswordRequest resetPasswordRequest);
}
