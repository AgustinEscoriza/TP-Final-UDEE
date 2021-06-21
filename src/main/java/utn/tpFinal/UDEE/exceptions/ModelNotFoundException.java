package utn.tpFinal.UDEE.exceptions;

import org.springframework.http.HttpStatus;

public class ModelNotFoundException extends GenericWebException {

    public ModelNotFoundException(String route, String method) {
        this.method=method;
        this.route=route;
        this.httpStatus = HttpStatus.NOT_FOUND;
    }

    @Override
    public String getMessage() {
        return "Model not found";
    }
}
