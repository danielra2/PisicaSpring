package mycode.pisicaspring.exceptions;
import mycode.pisicaspring.constants.PisicaConstant;
public class NoResultsTopQueryException extends RuntimeException {
    public NoResultsTopQueryException(){
        super(PisicaConstant.NO_RESULTS_TOP_QUERY);
    }
}