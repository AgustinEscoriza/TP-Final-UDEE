package utn.tpFinal.UDEE.exceptions;


import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.constraints.Null;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@org.springframework.web.bind.annotation.ControllerAdvice
public class ControllerAdvisor extends ResponseEntityExceptionHandler {

    @ExceptionHandler(GenericWebException.class)
    public ResponseEntity<Object> handleMeterNotFoundException(
            GenericWebException e, WebRequest webRequest){
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", e.getMessage());
        return handleExceptionInternal(e,body,new HttpHeaders(),e.httpStatus,webRequest);
    };

    @ExceptionHandler(ParseException.class)
    public ResponseEntity<Object> handleParseException(ParseException e, WebRequest webRequest){
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", e.getMessage());
        return handleExceptionInternal(e,body,new HttpHeaders(),HttpStatus.BAD_REQUEST,webRequest);
    };
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<Object> handleNullpointerException(ParseException e, WebRequest webRequest){
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", e.getMessage());
        return handleExceptionInternal(e,body,new HttpHeaders(),HttpStatus.BAD_REQUEST,webRequest);
    }
}
