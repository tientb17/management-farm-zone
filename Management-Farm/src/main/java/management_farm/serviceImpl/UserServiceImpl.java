package management_farm.serviceImpl;

import javassist.NotFoundException;
import management_farm.constants.FarmConstant;
import management_farm.dao.ForgotPasswordDao;
import management_farm.dao.RoleDao;
import management_farm.dao.UserDao;
import management_farm.jwt.CustomerUsersDetailsService;
import management_farm.jwt.JwtFilter;
import management_farm.jwt.JwtUtil;
import management_farm.model.ForgotPassword;
import management_farm.model.Role;
import management_farm.model.User;
import management_farm.request.*;
import management_farm.response.CustomerResponse;
import management_farm.service.UserService;
import management_farm.utils.EmailUtils;
import management_farm.utils.FarmUtils;
import management_farm.wrapper.UserMapper;
import org.apache.commons.validator.GenericValidator;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.regex.Pattern;

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
    private PasswordEncoder encoder;

    @Autowired
    EmailUtils emailUtils;

    @Autowired
    UserDao userDao;

    @Autowired
    RoleDao roleDao;

    @Autowired
    ForgotPasswordDao forgotPasswordDao;

    @Override
    public ResponseEntity<CustomerResponse<String>> login(LoginRequest loginRequest) {
        CustomerResponse<String> loginResponse = new CustomerResponse<>();
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
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    @Override
    public ResponseEntity<CustomerResponse<List<UserMapper>>> getAllUser() {
        CustomerResponse<List<UserMapper>> loginResponse = new CustomerResponse<>();
        try {
            if (jwtFilter.isAdmin()) {
                loginResponse.setSuccess(true);
                loginResponse.setMessage("Get All User Success");
                loginResponse.setTotal(userDao.getAllUser().size());
                loginResponse.setData(userDao.getAllUser());
                return ResponseEntity.status(HttpStatus.OK).body(loginResponse);
            } else {
                loginResponse.setMessage(FarmConstant.UNAUTHORIZED);
                loginResponse.setSuccess(false);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(loginResponse);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    @Override
    public ResponseEntity<CustomerResponse<String>> signup(SignupRequest signupRequest) {
        CustomerResponse<String> signResponse = new CustomerResponse<>();
        try {
            // validate signup info
            if (checkFieldRequired(signupRequest)) {
                // userLogin dose not existed -> create new
                if (Objects.isNull(userDao.findUserByUserLogin(signupRequest.getUserLogin()))) {
                    // check data input
                    if (signupRequest.getAddress().length() > 255) {
                        signResponse.setSuccess(false);
                        signResponse.setMessage("Địa chỉ " + FarmConstant.NOT_MORE_THAN_255_CHARACTER);
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(signResponse);
                    }
                    if (signupRequest.getEmail().length() > 255) {
                        signResponse.setSuccess(false);
                        signResponse.setMessage("Email " + FarmConstant.NOT_MORE_THAN_255_CHARACTER);
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(signResponse);
                    }
                    if (signupRequest.getEmail().length() > 20) {
                        signResponse.setSuccess(false);
                        signResponse.setMessage("Full name " + FarmConstant.NOT_MORE_THAN_20_CHARACTER);
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(signResponse);
                    }
                    if (signupRequest.getPhone().length() != 10) {
                        signResponse.setSuccess(false);
                        signResponse.setMessage("Phone " + FarmConstant.MUST_10_CHARACTER);
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(signResponse);
                    }
                    if (signupRequest.getEmail().length() > 20) {
                        signResponse.setSuccess(false);
                        signResponse.setMessage("User login " + FarmConstant.NOT_MORE_THAN_20_CHARACTER);
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(signResponse);
                    }
                    if (!GenericValidator.isDate(signupRequest.getBirthDate(), "dd/MM/yyyy", true)) {
                        signResponse.setSuccess(false);
                        signResponse.setMessage("Birth date " + FarmConstant.NOT_FORMAT_DATE);
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(signResponse);
                    }
                    if (!signupRequest.getPhone().chars().allMatch(Character::isDigit)) {
                        signResponse.setSuccess(false);
                        signResponse.setMessage("Phone " + FarmConstant.MUST_INPUT_NUMBER);
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(signResponse);
                    }
                    if (!Pattern.matches(FarmConstant.EMAIL_REGEX, signupRequest.getEmail())) {
                        signResponse.setSuccess(false);
                        signResponse.setMessage("Email " + FarmConstant.NOT_FORMAT_EMAIL);
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(signResponse);
                    }
                    Role role = roleDao.findByRoleName("user");
                    User userSave = new User();
                    userSave.setRoleId(role);
                    userSave.setAddress(signupRequest.getAddress());
                    userSave.setBirthDate(new SimpleDateFormat("dd/MM/yyyy").parse(signupRequest.getBirthDate()));
                    userSave.setCreateDate(new Date());
                    userSave.setEmail(signupRequest.getEmail());
                    userSave.setFullName(signupRequest.getFullName());
                    userSave.setPassword(encoder.encode(signupRequest.getPassword()));
                    userSave.setPhone(signupRequest.getPhone());
                    userSave.setUserLogin(signupRequest.getUserLogin());

                    userDao.save(userSave);

                    signResponse.setSuccess(true);
                    signResponse.setMessage(FarmConstant.ADD_SUCCESS);
                    return ResponseEntity.status(HttpStatus.OK).body(signResponse);
                } else {
                    signResponse.setSuccess(false);
                    signResponse.setMessage("User login " + FarmConstant.HAS_EXISTED);
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(signResponse);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    @Override
    public ResponseEntity<CustomerResponse<String>> updateAvatar(MultipartFile file, Long id) {
        CustomerResponse<String> updateAvatar = new CustomerResponse<>();
        try {
            // find user update avatar
            Optional<User> userUpdateAvatar = userDao.findById(id);
            if (userUpdateAvatar.isPresent()) {
                userDao.updateAvatar(file.getBytes(), id, new Date(), jwtFilter.getCurrentUser());

                updateAvatar.setSuccess(true);
                updateAvatar.setMessage(FarmConstant.UPDATE_SUCCESS + "với id: " + id);
                return ResponseEntity.status(HttpStatus.OK).body(updateAvatar);
            } else {
                updateAvatar.setSuccess(false);
                updateAvatar.setMessage(FarmConstant.NOT_FOUND_OBJECT + "với id: " + id);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(updateAvatar);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    @Override
    public ResponseEntity<byte[]> getAvatarById(Long id) {
        try {
            byte[] imageData = userDao.findById(id).orElseThrow(() ->
                    new IllegalArgumentException("Image not found")).getAvatar();
            return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(imageData);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return ResponseEntity.badRequest().body(null);
    }

    @Override
    public ResponseEntity<CustomerResponse<String>> updateStatus(Long id, UpdateStatusUserRequest status) {
        CustomerResponse<String> updateStatus = new CustomerResponse<>();
        try {
            if (jwtFilter.isAdmin()) {
                User userExisted = userDao.findById(id).orElseThrow(() ->
                        new NotFoundException(FarmConstant.NOT_FOUND_OBJECT + ": id" + id));
                userDao.updateStatus(id, status.getStatus(), new Date(), jwtFilter.getCurrentUser());

                // send mail to all user has admin role
                sendMailToAllAdmin(status.getStatus(),
                        userExisted.getEmail(),
                        userExisted.getUserLogin(),
                        userDao.getAllUserAdminRole());

                updateStatus.setMessage(FarmConstant.UPDATE_SUCCESS);
                updateStatus.setSuccess(true);
                return ResponseEntity.status(HttpStatus.OK).body(updateStatus);
            } else {
                updateStatus.setMessage(FarmConstant.UNAUTHORIZED);
                updateStatus.setSuccess(false);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(updateStatus);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    @Override
    public ResponseEntity<CustomerResponse<String>> forgotPassword(ForgotPasswordRequest forgotPasswordRequest) {
        CustomerResponse<String> forgotPassword = new CustomerResponse<>();
        try {
            if (Strings.isNotEmpty(forgotPasswordRequest.getUserLogin()) && Strings.isNotBlank(forgotPasswordRequest.getUserLogin())
                    && Strings.isNotEmpty(forgotPasswordRequest.getEmail()) && Strings.isNotBlank(forgotPasswordRequest.getEmail())) {
                // find user by userLogin and email
                User userExisted = userDao.findByUserLoginAndEmail(forgotPasswordRequest.getUserLogin().trim(), forgotPasswordRequest.getEmail().trim());
                if (!Objects.isNull(userExisted)) {
                    // generate token for reset pasword
                    String resetToken = generateResetToken();

                    // send email
                    emailUtils.forgotMail(userExisted.getEmail(), FarmConstant.SUBJECT_FOR_SEND_EMAIL, userExisted.getUserLogin(), resetToken);

                    // save data to db
                    // find user has existed in db -> yes: update; no -> add new
                    ForgotPassword forgotPasswordExisted = forgotPasswordDao.findByUserId(userExisted.getUserId());

                    if (Objects.isNull(forgotPasswordExisted)) {
                        // add new
                        forgotPasswordExisted = new ForgotPassword();
                        forgotPasswordExisted.setFirstDateForgot(new Date());
                    } else {
                        // update
                        forgotPasswordExisted.setFgPasswordId(forgotPasswordExisted.getFgPasswordId());
                    }
                    forgotPasswordExisted.setExpireDate(LocalDateTime.ofInstant(new Date().toInstant(), ZoneId.systemDefault()));
                    forgotPasswordExisted.setLastDateReset(new Date());
                    forgotPasswordExisted.setResetToken(resetToken);
                    forgotPasswordExisted.setUserId(userExisted);
                    forgotPasswordDao.save(forgotPasswordExisted);

                    forgotPassword.setSuccess(true);
                    forgotPassword.setMessage(FarmConstant.SEND_MAIL_SUCCESS);
                    return ResponseEntity.status(HttpStatus.OK).body(forgotPassword);
                } else {
                    forgotPassword.setSuccess(false);
                    forgotPassword.setMessage(FarmConstant.NOT_FOUND_OBJECT + "\nUser Login hoặc Email.");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(forgotPassword);
                }
            } else {
                forgotPassword.setSuccess(false);
                forgotPassword.setMessage(FarmConstant.IS_NOT_EMPTY_OR_BLANK);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(forgotPassword);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    @Override
    public ResponseEntity<CustomerResponse<String>> resetPassword(String resetToken, ResetPasswordRequest resetPasswordRequest) {
        CustomerResponse<String> resetResponse = new CustomerResponse<>();
        try {
            // find reset_token
            ForgotPassword resetTokenExisted = forgotPasswordDao.findByResetToken(resetToken);
            // check token is existed
            if (Objects.isNull(resetTokenExisted)) {
                resetResponse.setMessage(FarmConstant.INVALID_TOKEN);
                resetResponse.setSuccess(false);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resetResponse);
            }

            // check expire_date reset_token
            if (isTokenExpireDate(resetTokenExisted.getExpireDate())) {
                resetResponse.setMessage(FarmConstant.EXPIRE_DATE_TOKEN);
                resetResponse.setSuccess(false);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resetResponse);
            }

            // create new password
            Optional<User> userResetPassword = userDao.findById(resetTokenExisted.getUserId().getUserId());
            userDao.resetPassword(userResetPassword.get().getUserId(),
                    encoder.encode(resetPasswordRequest.getNewPassword()), new Date());

            resetResponse.setMessage(FarmConstant.UPDATE_SUCCESS);
            resetResponse.setSuccess(true);
            return ResponseEntity.status(HttpStatus.OK).body(resetResponse);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    private boolean isTokenExpireDate(final LocalDateTime expireDate) {
        LocalDateTime now = LocalDateTime.now();
        Duration diff = Duration.between(expireDate, now);
        return diff.toMinutes() >= Long.valueOf(FarmConstant.EXPIRE_TOKEN_AFTER_MINUTES);
    }

    private String generateResetToken() {
        StringBuilder token = new StringBuilder();
        return token.append(UUID.randomUUID())
                .append(UUID.randomUUID()).toString();
    }

    private boolean checkFieldRequired(SignupRequest signupRequest) {
        // check is null
        if (!Strings.isEmpty(signupRequest.getAddress()) &&
                !Strings.isEmpty(signupRequest.getBirthDate()) &&
                !Strings.isEmpty(signupRequest.getEmail()) &&
                !Strings.isEmpty(signupRequest.getFullName()) &&
                !Strings.isEmpty(signupRequest.getPassword()) &&
                !Strings.isEmpty(signupRequest.getPhone()) &&
                !Strings.isEmpty(signupRequest.getUserLogin())) return true;
        return false;
    }

    private void sendMailToAllAdmin(String status, String email, String userLogin, List<String> allAdmin) {
        allAdmin.remove(jwtFilter.getCurrentEmail());
        if (status != null && status.equalsIgnoreCase("true")) {
            emailUtils.sendSimpleMessage(jwtFilter.getCurrentEmail(),
                    "Account Approved", "Email:- " + email + "\n User Login:- "
                            + userLogin + "\nis approved by \nADMIN:-" + jwtFilter.getCurrentEmail(), allAdmin);
        } else {
            emailUtils.sendSimpleMessage(jwtFilter.getCurrentEmail(),
                    "Account Disabled", "Email:- " + email + "\n User Login:- "
                            + userLogin + "\nis disabled by \nADMIN:-" + jwtFilter.getCurrentEmail(), allAdmin);
        }
    }
}
