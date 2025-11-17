package mycode.pisicaspring.service;

import mycode.pisicaspring.dtos.PisicaDto;
import mycode.pisicaspring.dtos.PisicaResponse;
import mycode.pisicaspring.exceptions.PisicaAlreadyExistsException;
import mycode.pisicaspring.exceptions.PisicaDoesntExistException;

public interface PisicaCommandService {
    PisicaResponse createPisica(PisicaDto pisicaDto) throws PisicaAlreadyExistsException;
    PisicaResponse deletePisicaByName(PisicaDto pisicaDto) throws PisicaDoesntExistException;
    PisicaResponse updatePisicaByName(String currentName,PisicaDto pisicaDto) throws PisicaDoesntExistException;

}
