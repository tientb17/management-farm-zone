package management_farm.serviceImpl;

import management_farm.constants.FarmConstant;
import management_farm.dao.UserDao;
import management_farm.jwt.CustomerUsersDetailsService;
import management_farm.jwt.JwtFilter;
import management_farm.jwt.JwtUtil;
import management_farm.request.LoginRequest;
import management_farm.response.LoginResponse;
import management_farm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * Date: 11/24/2023
 * Project: Management-Farm
 * Author: ManhTien
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    CustomerUsersDetailsService customerUsersDetailsService;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    JwtFilter jwtFilter;

    @Autowired
    UserDao userDao;

    @Override
    public ResponseEntity<LoginResponse<String>> login(LoginRequest loginRequest) {
        LoginResponse<String> loginResponse = new LoginResponse<>();
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUserLogin(), loginRequest.getPassword()));
            if (authentication.isAuthenticated()) {
                if (customerUsersDetailsService.getUserDetail().getStatus().equalsIgnoreCase("true")) {
                    loginResponse.setSuccess(true);
                    loginResponse.setMessage("Generated successful.");
                    loginResponse.setData("Token: " + jwtUtil.generateToken(customerUsersDetailsService.getUserDetail().getUserLogin(),
                            customerUsersDetailsService.getUserDetail().getRoleId().getRoleName(), customerUsersDetailsService.getUserDetail().getEmail()));
                    return ResponseEntity.status(HttpStatus.OK).body(loginResponse);
                } else {
                    loginResponse.setMessage("Something went wrong");
                    loginResponse.setSuccess(false);
                    loginResponse.setData("{\"message\":\"" + "Wait for admin approval." + "\"}");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(loginResponse);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        loginResponse.setSuccess(false);
        loginResponse.setMessage(FarmConstant.SOMETHING_WENT_WRONG);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(loginResponse);
    }

    @Override
    public ResponseEntity<LoginResponse<String>> getAllUser() {
        LoginResponse<String> loginResponse = new LoginResponse<>();
        try {
            if (jwtFilter.isAdmin()) {
                loginResponse.setSuccess(true);
                loginResponse.setMessage("Get All User Success");
                loginResponse.setTotal(userDao.findAll().size());
                loginResponse.setData(userDao.findAll().toString());
                return ResponseEntity.status(HttpStatus.OK).body(loginResponse);
            } else {
                loginResponse.setMessage(FarmConstant.UNAUTHORIZED);
                loginResponse.setSuccess(false);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(loginResponse);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        loginResponse.setSuccess(false);
        loginResponse.setMessage(FarmConstant.SOMETHING_WENT_WRONG);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(loginResponse);
    }
}
