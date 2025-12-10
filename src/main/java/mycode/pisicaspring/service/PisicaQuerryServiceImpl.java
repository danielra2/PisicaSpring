package mycode.pisicaspring.service;

import mycode.pisicaspring.dtos.*;
import mycode.pisicaspring.exceptions.*;
import mycode.pisicaspring.mappers.PisicaManualMapper;
import mycode.pisicaspring.models.Pisica;
import mycode.pisicaspring.repository.PisicaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class PisicaQuerryServiceImpl implements PisicaQuerryService {
    private PisicaRepository pisicaRepository;
    private PisicaManualMapper pisicaManualMapper;

    public PisicaQuerryServiceImpl(PisicaRepository pisicaRepository,PisicaManualMapper pisicaManualMapper){
        this.pisicaRepository=pisicaRepository;
        this.pisicaManualMapper=pisicaManualMapper;
    }


    @Override
    public PisicaListResponsePageable getAllPisici(int page,int size) {
        PageRequest pageable=PageRequest.of(page,size);
        Page<Pisica> pisicaPage=pisicaRepository.findAll(pageable);
        List<PisicaResponse>pisicaDtoList=pisicaManualMapper.mapPisicaListToResponseList(pisicaPage.getContent());
        return new PisicaListResponsePageable(
                pisicaDtoList,
                pisicaPage.getTotalElements(),
                pisicaPage.getTotalPages(),
                pisicaPage.getNumber(),
                pisicaPage.getSize()
        );

    }

    @Override
        public List<PisicaNumeVarstaDto> getOlderPisiciInfo(int varstaMinima) {
            return pisicaRepository.findPisicasByVarstaGreaterThan(varstaMinima);
        }

    @Override
    public PisicaRasaListRequest findAllUniqueRase() throws RasaIsEmpty {
        List<Pisica>pisicaList= pisicaRepository.getAllPisica();
        if (pisicaList == null || pisicaList.isEmpty()) {
            throw new RasaIsEmpty();
        }
        List<PisicaRasaInfo> raseInfoList = pisicaManualMapper.mapperPisicaListToRasaInfoList(pisicaList);

        return new PisicaRasaListRequest(raseInfoList);
    }

    @Override
    public PisicaVarstaRangeListRequest findPisiciByVarstaRange(int varstaMin, int varstaMax) {
        List<Pisica> pisicaList = pisicaRepository.findByVarstaBetween(varstaMin, varstaMax);
        List<PisicaVarstaRangeInfo> infoList = pisicaManualMapper.mapperPisicaListToVarstaRangeInfoList(pisicaList);
        return new PisicaVarstaRangeListRequest(infoList);
    }

    @Override
    public PisicaListRequest getAllByPisiciOrderedByVarsta() {
        List<Pisica>pisicaList=pisicaRepository.findAllByOrderByVarstaAsc();
        List<PisicaDto>pisicaDtoList=pisicaManualMapper.mapperPisicaListToPisicaDtoList(pisicaList);
        return new PisicaListRequest(pisicaDtoList);
    }
    public PisicaNumeVarstaListRequest findByRasaSortedByVarsta(String rasa) throws RasaNotFoundException {
        List<Pisica> pisicaList = pisicaRepository.findByRasaOrderByVarstaAsc(rasa);
        if (pisicaList.isEmpty()) {
            throw new RasaNotFoundException();
        }
        List<PisicaNumeVarstaInfo> infoList = pisicaManualMapper.mapperPisicaListToNumeVarstaInfoList(pisicaList);
        return new PisicaNumeVarstaListRequest(infoList);
    }

    @Override
    public PisicaListRequest findTop3YoungestPisici() throws PisicaDoesntExistException {
        List<Pisica> pisicaList = pisicaRepository.findTop3ByOrderByVarstaAsc();
        if(pisicaList.isEmpty()){
            throw new PisicaDoesntExistException();
        }
        List<PisicaDto> pisicaDtoList = this.pisicaManualMapper.mapperPisicaListToPisicaDtoList(pisicaList);
        return new PisicaListRequest(pisicaDtoList);
    }
    @Override
    public PisicaNumeRasaListRequest findByNumeStartingWith(String nume)throws NoPisicaFoundException{
        List<Pisica> pisicaList=pisicaRepository.findByNumeStartingWith(nume);
        if(pisicaList.isEmpty()){
            throw new NoPisicaFoundException();
        }
        List<PisicaNumeRasaInfo> infoList=pisicaManualMapper.mapperPisicaListToNumeRasaInfoList(pisicaList);
        return new PisicaNumeRasaListRequest(infoList);
    }

    @Override
    public PisicaIdNumeRasaListRequest findByVarstaExact(int varsta)throws NoPisicaFoundException{
        List<Pisica> pisicaList=pisicaRepository.findByVarsta(varsta);
        if(pisicaList.isEmpty()){
            throw new NoPisicaFoundException();
        }
        List<PisicaIdNumeRasaInfo> infoList=pisicaManualMapper.mapperPisicaListToIdNumeRasaInfoList(pisicaList);
        return new PisicaIdNumeRasaListRequest(infoList);
    }

    @Override
    public PisicaListRequest findTop5OldestPisici()throws NoResultsTopQueryException {
        List<Pisica> pisicaList=pisicaRepository.findTop5ByOrderByVarstaDesc();
        if(pisicaList.isEmpty()){
            throw new NoResultsTopQueryException();
        }
        List<PisicaDto> pisicaDtoList=this.pisicaManualMapper.mapperPisicaListToPisicaDtoList(pisicaList);
        return new PisicaListRequest(pisicaDtoList);
    }

    @Override
    public RasaAverageAgeListRequest findAverageAgeByRasa() throws NoResultsTopQueryException {
        List<RasaAverageAgeInfo> infoList=pisicaRepository.findRasaAverageAge();
        if(infoList.isEmpty()){
            throw new NoResultsTopQueryException();
        }
        return new RasaAverageAgeListRequest(infoList);
    }

}
