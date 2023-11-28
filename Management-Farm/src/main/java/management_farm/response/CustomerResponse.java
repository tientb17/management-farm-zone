package management_farm.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Date: 11/24/2023
 * Project: Management-Farm
 * Author: ManhTien
 */

@Getter
@Setter
public class CustomerResponse<T> {
    private boolean success;
    private String message;
    private int total;
    private T data;
    private List<T> lstData;
}
