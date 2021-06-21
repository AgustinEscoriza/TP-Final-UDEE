package utn.tpFinal.UDEE.exceptions;

import org.springframework.http.HttpStatus;

public class UnauthorizedToAddEmployeeException extends GenericWebException {

    public UnauthorizedToAddEmployeeException(String route, String method) {
        this.method=method;
        this.route=route;
        this.httpStatus = HttpStatus.UNAUTHORIZED;
    }

    @Override
    public String getMessage() {
        return "Employees cant be added by other Employees";
    }
}
