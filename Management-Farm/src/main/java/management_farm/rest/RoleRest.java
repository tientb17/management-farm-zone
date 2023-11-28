package management_farm.rest;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import management_farm.model.Role;
import management_farm.request.CreateRoleRequest;
import management_farm.response.CustomerResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Date: 11/27/2023
 * Project: Management-Farm
 * Author: ManhTien
 */
@RequestMapping("/roles")
public interface RoleRest {
    @GetMapping("/getAllRole")
    @ApiOperation(value = "Get All Role", authorizations = { @Authorization(value="Bearer") })
    ResponseEntity<CustomerResponse<Role>> getAllRole();

    @PostMapping("/create")
    @ApiOperation(value = "Create New Role", authorizations = { @Authorization(value="Bearer") })
    ResponseEntity<CustomerResponse<String>> create(@RequestBody CreateRoleRequest createRoleRequest);
}
