package mycode.pisicaspring.exceptions;

import mycode.pisicaspring.constants.PisicaConstant;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class PisicaAlreadyExistsException extends RuntimeException {
    public PisicaAlreadyExistsException() {
        super(PisicaConstant.PISICA_ALREADY_EXISTS);
    }
}
