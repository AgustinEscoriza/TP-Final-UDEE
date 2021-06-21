package utn.tpFinal.UDEE.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@Builder
public class PostResponse {

    private String url;
    private HttpStatus status;

}
