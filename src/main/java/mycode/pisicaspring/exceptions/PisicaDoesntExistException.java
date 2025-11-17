package mycode.pisicaspring.exceptions;

import mycode.pisicaspring.constants.PisicaConstant;

public class PisicaDoesntExistException extends RuntimeException {
    public PisicaDoesntExistException() {
        super(PisicaConstant.PISICA_DOESNT_EXIST);
    }
}
