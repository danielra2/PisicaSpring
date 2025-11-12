package mycode.pisicaspring.exceptions;

import mycode.pisicaspring.constants.PisicaConstant;

public class PisicaAlreadyExistsException extends RuntimeException {
    public PisicaAlreadyExistsException(String message) {
        super(PisicaConstant.PISICA_ALREADY_EXISTS);
    }
}
