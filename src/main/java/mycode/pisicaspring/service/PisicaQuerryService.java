package mycode.pisicaspring.service;

import mycode.pisicaspring.dtos.*;
import mycode.pisicaspring.exceptions.*;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PisicaQuerryService {
    PisicaListResponsePageable getAllPisici(Pageable pageable);

    List<PisicaNumeVarstaDto>getOlderPisiciInfo(int varstaMinima, Pageable pageable);
    PisicaRasaListRequest findAllUniqueRase(Pageable pageable) throws RasaIsEmpty;
    PisicaVarstaRangeListRequest findPisiciByVarstaRange(int varstaMin, int varstaMax, Pageable pageable);
    PisicaListRequest getAllByPisiciOrderedByVarsta(Pageable pageable);
    PisicaNumeVarstaListRequest findByRasaSortedByVarsta(String rasa, Pageable pageable) throws RasaNotFoundException;
    PisicaListRequest findTop3YoungestPisici() throws PisicaDoesntExistException;
    PisicaNumeRasaListRequest findByNumeStartingWith(String nume, Pageable pageable)throws NoPisicaFoundException;
    PisicaIdNumeRasaListRequest findByVarstaExact(int varsta, Pageable pageable)throws NoPisicaFoundException;
    PisicaListRequest findTop5OldestPisici()throws NoResultsTopQueryException;
    RasaAverageAgeListRequest findAverageAgeByRasa(Pageable pageable)throws NoResultsTopQueryException;





}
