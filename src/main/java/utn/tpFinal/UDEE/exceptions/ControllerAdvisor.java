/*
package utn.tpFinal.UDEE.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@org.springframework.web.bind.annotation.ControllerAdvice
public class ControllerAdvisor extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UnauthorizedToAddEmployeeException.class)
    public ResponseEntity<Object> handleUnauthorizedToAddEmployeeException(
            UnauthorizedToAddEmployeeException ex, WebRequest request) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", "Employees cannot add other employees");

        return new ResponseEntity<>(body, HttpStatus.UNAUTHORIZED);
    }
}
*/
