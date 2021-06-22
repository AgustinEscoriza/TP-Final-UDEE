package utn.tpFinal.UDEE.exceptions;

import org.springframework.http.HttpStatus;

public class MeterAlreadyHasResidenceException extends GenericWebException {
    public MeterAlreadyHasResidenceException(String route, String method) {
        this.method=method;
        this.route=route;
        this.httpStatus = HttpStatus.BAD_REQUEST;
    }

    @Override
    public String getMessage() {
        return "Meter Already Has a Residence assigned";
    }
}
