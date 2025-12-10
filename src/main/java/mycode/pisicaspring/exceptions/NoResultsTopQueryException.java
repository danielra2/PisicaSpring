package mycode.pisicaspring.exceptions;

import mycode.pisicaspring.constants.PisicaConstant;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NoResultsTopQueryException extends RuntimeException {
    public NoResultsTopQueryException(){
        super(PisicaConstant.NO_RESULTS_TOP_QUERY);
    }
}
