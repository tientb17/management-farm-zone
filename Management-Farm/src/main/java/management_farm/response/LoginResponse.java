package management_farm.response;

import lombok.Getter;
import lombok.Setter;

/**
 * Date: 11/24/2023
 * Project: Management-Farm
 * Author: ManhTien
 */

@Getter
@Setter
public class LoginResponse<T> {
    private boolean success;
    private String message;
    private int total;
    private T data;
}
