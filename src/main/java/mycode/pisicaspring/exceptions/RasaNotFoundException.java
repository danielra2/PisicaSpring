package mycode.pisicaspring.exceptions;

import mycode.pisicaspring.constants.PisicaConstant;

public class RasaNotFoundException extends RuntimeException {
    public RasaNotFoundException() {
        super(PisicaConstant.RASA_NOT_FOUND);
    }
}
