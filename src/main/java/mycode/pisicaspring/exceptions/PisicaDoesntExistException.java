package mycode.pisicaspring.exceptions;

import mycode.pisicaspring.constants.PisicaConstant;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PisicaDoesntExistException extends RuntimeException {
    public PisicaDoesntExistException() {
        super(PisicaConstant.PISICA_DOESNT_EXIST);
    }
}
