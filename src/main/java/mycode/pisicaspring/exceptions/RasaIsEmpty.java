package mycode.pisicaspring.exceptions;

import mycode.pisicaspring.constants.PisicaConstant;

public class RasaIsEmpty extends RuntimeException {
    public RasaIsEmpty() {
        super(PisicaConstant.RASA_IS_EMPTY);
    }
}
