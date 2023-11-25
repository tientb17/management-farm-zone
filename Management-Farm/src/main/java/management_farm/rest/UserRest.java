package management_farm.rest;

import management_farm.model.User;
import management_farm.request.LoginRequest;
import management_farm.response.LoginResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

/**
 * Date: 11/24/2023
 * Project: Management-Farm
 * Author: ManhTien
 */
@RequestMapping("/users")
public interface UserRest {
    @PostMapping("/login")
    ResponseEntity<LoginResponse<String>> login(@RequestBody LoginRequest loginRequest);

    @GetMapping("/getAllUser")
    ResponseEntity<LoginResponse<String>> getAllUser();
}
