package utn.tpFinal.UDEE.exceptions;

import org.springframework.http.HttpStatus;

public class FeeTypeNotFoundException extends GenericWebException {
    public FeeTypeNotFoundException(String method, String route) {
        this.method=method;
        this.route=route;
        this.httpStatus = HttpStatus.NOT_FOUND;
    }

    @Override
    public String getMessage() {
        return "Fee Type not found";
    }
}
