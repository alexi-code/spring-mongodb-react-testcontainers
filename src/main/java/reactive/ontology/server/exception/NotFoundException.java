package reactive.ontology.server.exception;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
@Log4j2
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
        log.warn("NotFoundException: {}", message);
    }
}
