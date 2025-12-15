package mycode.pisicaspring.exceptions;

import mycode.pisicaspring.constants.PisicaConstant;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RasaNotFoundException extends RuntimeException {
    public RasaNotFoundException() {
        super(PisicaConstant.RASA_NOT_FOUND);
    }
}
