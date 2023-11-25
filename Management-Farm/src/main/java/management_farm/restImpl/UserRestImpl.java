package management_farm.restImpl;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import management_farm.model.User;
import management_farm.request.LoginRequest;
import management_farm.response.LoginResponse;
import management_farm.rest.UserRest;
import management_farm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

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
    public ResponseEntity<LoginResponse<String>> login(LoginRequest loginRequest) {
        return userService.login(loginRequest);
    }

    @Override
    @ApiOperation(value = "Get some data", authorizations = { @Authorization(value="Bearer") })
    public ResponseEntity<LoginResponse<String>> getAllUser() {
        return userService.getAllUser();
    }
}
