package reactive.ontology.server.exception;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_GATEWAY)
@Log4j2
public class BadResponseException extends RuntimeException {
    public BadResponseException(String message) {
        super(message);
        log.warn("BadResponseException: {}", message);
    }
}
