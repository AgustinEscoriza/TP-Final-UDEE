package utn.tpFinal.UDEE.exceptions;

import org.springframework.http.HttpStatus;

public class UserAuthenticationFailedException extends GenericWebException{
    public UserAuthenticationFailedException(String route, String method) {
        this.method=method;
        this.route=route;
        this.httpStatus = HttpStatus.BAD_REQUEST;
    }

    @Override
    public String getMessage() {
        return "Wrong DNI / You are not an employee";
    }
}
