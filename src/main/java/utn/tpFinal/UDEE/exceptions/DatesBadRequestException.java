package utn.tpFinal.UDEE.exceptions;

import org.springframework.http.HttpStatus;

public class DatesBadRequestException extends GenericWebException{

    public DatesBadRequestException(String method, String route) {
        this.method=method;
        this.route=route;
        this.httpStatus = HttpStatus.BAD_REQUEST;
    }

    @Override
    public String getMessage() {
        return "Wrong Date Input";
    }
}
