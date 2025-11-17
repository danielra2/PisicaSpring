package mycode.pisicaspring.exceptions;

import mycode.pisicaspring.constants.PisicaConstant;

public class PisicaAlreadyExistsException extends RuntimeException {
    public PisicaAlreadyExistsException() {
        super(PisicaConstant.PISICA_ALREADY_EXISTS);
    }
}
