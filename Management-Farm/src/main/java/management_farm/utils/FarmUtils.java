package management_farm.utils;

import management_farm.constants.FarmConstant;
import management_farm.response.CustomerResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Date: 11/24/2023
 * Project: Management-Farm
 * Author: ManhTien
 */
public class FarmUtils {
    public FarmUtils() {
    }
    public static ResponseEntity<String> getResponseEntity(String responseMessage, HttpStatus httpStatus) {
        return new ResponseEntity<>("{\"message\":\"" + responseMessage + "\"}", httpStatus);
    }
}
