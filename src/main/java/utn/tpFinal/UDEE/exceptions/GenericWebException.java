package utn.tpFinal.UDEE.exceptions;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public abstract class GenericWebException extends Exception {
    String route;
    String method;
    HttpStatus httpStatus;
}
