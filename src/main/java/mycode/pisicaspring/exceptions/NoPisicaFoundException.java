package mycode.pisicaspring.exceptions;
import mycode.pisicaspring.constants.PisicaConstant;
public class NoPisicaFoundException extends RuntimeException {
    public NoPisicaFoundException(){
        super(PisicaConstant.NO_PISICA_FOUND);
    }
}