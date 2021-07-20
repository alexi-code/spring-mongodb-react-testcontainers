package reactive.ontology.server.exception;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
@Log4j2
public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
        log.warn("BadRequestException: {}", message);
    }
}
