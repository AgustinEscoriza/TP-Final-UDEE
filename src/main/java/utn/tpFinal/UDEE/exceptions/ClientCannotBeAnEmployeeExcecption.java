package utn.tpFinal.UDEE.exceptions;

import org.springframework.http.HttpStatus;

public class ClientCannotBeAnEmployeeExcecption extends GenericWebException{
    public ClientCannotBeAnEmployeeExcecption(String method,String route){
        this.method=method;
        this.route=route;
        this.httpStatus = HttpStatus.BAD_REQUEST;
    }
    @Override
    public String getMessage() {
        return "Employee cannot be a client ";
    }
}
