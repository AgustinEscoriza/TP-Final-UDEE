package utn.tpFinal.UDEE.exceptions;

import org.springframework.http.HttpStatus;

public class BrandNotFoundException extends GenericWebException{

    public BrandNotFoundException(String route, String method) {
        this.method=method;
        this.route=route;
        this.httpStatus = HttpStatus.NOT_FOUND;
    }

    @Override
    public String getMessage() {
        return "Meter Brand not found";
    }
}
