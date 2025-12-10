package mycode.pisicaspring.exceptions;

import mycode.pisicaspring.constants.PisicaConstant;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NoPisicaFoundException extends RuntimeException {
    public NoPisicaFoundException(){
        super(PisicaConstant.NO_PISICA_FOUND);
    }
}
