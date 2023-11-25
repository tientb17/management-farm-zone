package management_farm.service;

import management_farm.model.User;
import management_farm.request.LoginRequest;
import management_farm.response.LoginResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

/**
 * Date: 11/24/2023
 * Project: Management-Farm
 * Author: ManhTien
 */
public interface UserService {
    ResponseEntity<LoginResponse<String>> login(LoginRequest loginRequest);

    ResponseEntity<LoginResponse<String>> getAllUser();
}
