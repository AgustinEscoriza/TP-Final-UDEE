package utn.tpFinal.UDEE.exceptions;

import org.springframework.http.HttpStatus;

public class ResidenceAlreadyExists extends GenericWebException{

    public ResidenceAlreadyExists(String route, String method) {
        this.method=method;
        this.route=route;
        this.httpStatus = HttpStatus.NOT_FOUND;
    }

    @Override
    public String getMessage() {
        return "A Residence Already exist with that ID";
    }
}
