package utn.tpFinal.UDEE.exceptions;

import org.springframework.http.HttpStatus;

public class ClientAlreadyExistsException extends GenericWebException {

    public ClientAlreadyExistsException(String route, String method) {
        this.method=method;
        this.route=route;
        this.httpStatus = HttpStatus.BAD_REQUEST;
    }

    @Override
    public String getMessage() {
        return "Client Already Exists";
    }
}
