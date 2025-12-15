package mycode.pisicaspring.exceptions;

import mycode.pisicaspring.constants.PisicaConstant;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RasaIsEmpty extends RuntimeException {
    public RasaIsEmpty() {
        super(PisicaConstant.RASA_IS_EMPTY);
    }
}
