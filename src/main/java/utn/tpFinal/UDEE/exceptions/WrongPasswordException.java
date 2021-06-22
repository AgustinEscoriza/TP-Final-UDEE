package utn.tpFinal.UDEE.exceptions;

import org.springframework.http.HttpStatus;

public class WrongPasswordException extends GenericWebException {
    public WrongPasswordException(String route, String method){
        this.method=method;
        this.route=route;
        this.httpStatus = HttpStatus.BAD_REQUEST;
    }
    @Override
    public String getMessage(){
        return "Incorrect Password";
    }
}
