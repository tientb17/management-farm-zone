package management_farm.service;

import management_farm.model.Role;
import management_farm.request.CreateRoleRequest;
import management_farm.response.CustomerResponse;
import org.springframework.http.ResponseEntity;

/**
 * Date: 11/27/2023
 * Project: Management-Farm
 * Author: ManhTien
 */
public interface RoleService {
    ResponseEntity<CustomerResponse<Role>> getAllRole();

    ResponseEntity<CustomerResponse<String>> create(CreateRoleRequest createRoleRequest);
}
