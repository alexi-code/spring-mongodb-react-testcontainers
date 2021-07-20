package reactive.ontology.server.exception;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
@Log4j2
public class AlreadyExistException extends RuntimeException {
    public AlreadyExistException(String message) {
        super(message);
        log.warn("AlreadyExistException: {}", message);
    }
}
