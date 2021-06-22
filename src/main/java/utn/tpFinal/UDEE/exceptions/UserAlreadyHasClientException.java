package utn.tpFinal.UDEE.exceptions;

import org.springframework.http.HttpStatus;

public class UserAlreadyHasClientException extends GenericWebException{
    public UserAlreadyHasClientException(String route, String method) {
        this.method=method;
        this.route=route;
        this.httpStatus = HttpStatus.BAD_REQUEST;
    }

    @Override
    public String getMessage() {
        return "That User Already Has a Client";
    }
}
