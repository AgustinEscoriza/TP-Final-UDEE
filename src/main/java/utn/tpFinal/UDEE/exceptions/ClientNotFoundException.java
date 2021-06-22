package utn.tpFinal.UDEE.exceptions;

import org.springframework.http.HttpStatus;

public class ClientNotFoundException extends GenericWebException{

    public ClientNotFoundException(String route, String method) {
        this.method=method;
        this.route=route;
        this.httpStatus = HttpStatus.NOT_FOUND;
    }

    @Override
    public String getMessage() {
        return "Client not found";
    }
}
