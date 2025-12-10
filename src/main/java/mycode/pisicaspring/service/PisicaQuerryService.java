package mycode.pisicaspring.service;

import mycode.pisicaspring.dtos.*;
import mycode.pisicaspring.exceptions.*;

import java.util.List;

public interface PisicaQuerryService {
    PisicaListResponsePageable getAllPisici(int page,int size);

// alta modalitate(probabil gresita)
    List<PisicaNumeVarstaDto>getOlderPisiciInfo(int varstaMinima);
    PisicaRasaListRequest findAllUniqueRase() throws RasaIsEmpty;
    PisicaVarstaRangeListRequest findPisiciByVarstaRange(int varstaMin, int varstaMax);
    PisicaListRequest getAllByPisiciOrderedByVarsta();
    PisicaNumeVarstaListRequest findByRasaSortedByVarsta(String rasa) throws RasaNotFoundException;
    PisicaListRequest findTop3YoungestPisici() throws PisicaDoesntExistException;
    PisicaNumeRasaListRequest findByNumeStartingWith(String nume)throws NoPisicaFoundException;
    PisicaIdNumeRasaListRequest findByVarstaExact(int varsta)throws NoPisicaFoundException;
    PisicaListRequest findTop5OldestPisici()throws NoResultsTopQueryException;
    RasaAverageAgeListRequest findAverageAgeByRasa()throws NoResultsTopQueryException;





}
