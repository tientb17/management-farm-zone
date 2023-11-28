package management_farm.serviceImpl;

import management_farm.constants.FarmConstant;
import management_farm.dao.RoleDao;
import management_farm.jwt.JwtFilter;
import management_farm.model.Role;
import management_farm.request.CreateRoleRequest;
import management_farm.response.CustomerResponse;
import management_farm.service.RoleService;
import management_farm.utils.FarmUtils;
import org.apache.logging.log4j.util.Strings;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;

/**
 * Date: 11/27/2023
 * Project: Management-Farm
 * Author: ManhTien
 */
@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    RoleDao roleDao;
    @Autowired
    JwtFilter jwtFilter;

    @Override
    public ResponseEntity<CustomerResponse<Role>> getAllRole() {
        CustomerResponse<Role> roleResponse = new CustomerResponse<>();
        try {
            // has admin role
            if (jwtFilter.isAdmin()) {
                roleResponse.setSuccess(true);
                roleResponse.setMessage(FarmConstant.GET_DATA_SUCCESS);
                roleResponse.setTotal(roleDao.findAll().size());
                roleResponse.setLstData(roleDao.findAll());

                return ResponseEntity.status(HttpStatus.OK).body(roleResponse);
            } else {
                roleResponse.setSuccess(false);
                roleResponse.setMessage(FarmConstant.UNAUTHORIZED);

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(roleResponse);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    @Override
    public ResponseEntity<CustomerResponse<String>> create(CreateRoleRequest createRoleRequest) {
        CustomerResponse<String> roleRequest = new CustomerResponse<>();
        try {
            if (jwtFilter.isAdmin()) {
                if (Strings.isEmpty(createRoleRequest.getName()) && Strings.isBlank(createRoleRequest.getName())) {
                    roleRequest.setMessage("Tên role " + FarmConstant.IS_NOT_EMPTY_OR_BLANK);
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(roleRequest);
                }
                // check role has existed
                if (Objects.isNull(roleDao.findByRoleName(createRoleRequest.getName()))) {
                    Role roleAdd = new Role();
                    roleAdd.setRoleName(createRoleRequest.getName());
                    roleAdd.setStatus("true");
                    roleAdd.setCreateDate(new Date());
                    roleAdd.setCreatedBy(jwtFilter.getCurrentUser());
                    roleDao.save(roleAdd);

                    roleRequest.setMessage(FarmConstant.ADD_SUCCESS);
                    roleRequest.setSuccess(true);

                    return ResponseEntity.status(HttpStatus.OK).body(roleRequest);
                } else {
                    roleRequest.setSuccess(false);
                    roleRequest.setMessage("Tên role: " + createRoleRequest.getName() + " " + FarmConstant.HAS_EXISTED);

                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(roleRequest);
                }
            } else {
                roleRequest.setSuccess(false);
                roleRequest.setMessage(FarmConstant.UNAUTHORIZED);

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(roleRequest);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

}
