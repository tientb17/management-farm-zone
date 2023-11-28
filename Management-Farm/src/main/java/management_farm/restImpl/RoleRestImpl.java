package management_farm.restImpl;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import management_farm.model.Role;
import management_farm.request.CreateRoleRequest;
import management_farm.response.CustomerResponse;
import management_farm.rest.RoleRest;
import management_farm.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * Date: 11/27/2023
 * Project: Management-Farm
 * Author: ManhTien
 */
@RestController
public class RoleRestImpl implements RoleRest {
    @Autowired
    RoleService roleService;

    @Override
    @ApiOperation(value = "Get All Role", authorizations = { @Authorization(value="Bearer") })
    public ResponseEntity<CustomerResponse<Role>> getAllRole() {
        return roleService.getAllRole();
    }

    @Override
    public ResponseEntity<CustomerResponse<String>> create(CreateRoleRequest createRoleRequest) {
        return roleService.create(createRoleRequest);
    }
}
