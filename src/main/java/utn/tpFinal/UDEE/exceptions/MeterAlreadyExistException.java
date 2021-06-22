package utn.tpFinal.UDEE.exceptions;

import org.springframework.http.HttpStatus;

public class MeterAlreadyExistException extends GenericWebException{
    public MeterAlreadyExistException(String route, String method) {
        this.method=method;
        this.route=route;
        this.httpStatus = HttpStatus.NOT_FOUND;
    }

    @Override
    public String getMessage() {
        return "Meter Already Exist";
    }
}
