package mycode.pisicaspring.service;

import lombok.extern.slf4j.Slf4j;
import mycode.pisicaspring.dtos.*;
import mycode.pisicaspring.exceptions.*;
import mycode.pisicaspring.mappers.PisicaManualMapper;
import mycode.pisicaspring.models.Pisica;
import mycode.pisicaspring.repository.PisicaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
@Slf4j
public class PisicaQuerryServiceImpl implements PisicaQuerryService {
    private PisicaRepository pisicaRepository;
    private PisicaManualMapper pisicaManualMapper;

    public PisicaQuerryServiceImpl(PisicaRepository pisicaRepository,PisicaManualMapper pisicaManualMapper){
        this.pisicaRepository=pisicaRepository;
        this.pisicaManualMapper=pisicaManualMapper;
    }


    @Override
    public PisicaListResponsePageable getAllPisici(Pageable pageable) {
        Pageable effective = pageable == null ? Pageable.unpaged() : pageable;
        log.debug("Listare pisici - page:{} size:{} sort:{}", effective.getPageNumber(), effective.getPageSize(), effective.getSort());
        Page<Pisica> pisicaPage = pisicaRepository.findAll(effective);
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
    public List<PisicaNumeVarstaDto> getOlderPisiciInfo(int varstaMinima, Pageable pageable) {
        Pageable effective = pageable == null ? Pageable.unpaged() : pageable;
        log.debug("Caut pisici mai batrane de {} ani", varstaMinima);
        Page<Pisica> pisicaPage = pisicaRepository.findByVarstaGreaterThan(varstaMinima, effective);
        return pisicaPage.getContent().stream()
                .map(p -> new PisicaNumeVarstaDto(p.getNume(), p.getVarsta()))
                .toList();
    }

    @Override
    public PisicaRasaListRequest findAllUniqueRase(Pageable pageable) throws RasaIsEmpty {
        Pageable effective = pageable == null ? Pageable.unpaged() : pageable;
        Page<String> rasaPage = pisicaRepository.findDistinctRase(effective);
        if (rasaPage.getTotalElements() == 0) {
            log.warn("Nu exista rase in baza de date");
            throw new RasaIsEmpty();
        }
        List<PisicaRasaInfo> raseInfoList = rasaPage.getContent().stream()
                .map(PisicaRasaInfo::new)
                .toList();

        return new PisicaRasaListRequest(raseInfoList);
    }

    @Override
    public PisicaVarstaRangeListRequest findPisiciByVarstaRange(int varstaMin, int varstaMax, Pageable pageable) {
        Pageable effective = pageable == null ? Pageable.unpaged() : pageable;
        log.debug("Caut pisici intre {} si {} ani", varstaMin, varstaMax);
        Page<Pisica> pisicaPage = pisicaRepository.findByVarstaBetween(varstaMin, varstaMax, effective);
        List<PisicaVarstaRangeInfo> infoList = pisicaManualMapper.mapperPisicaListToVarstaRangeInfoList(pisicaPage.getContent());
        return new PisicaVarstaRangeListRequest(infoList);
    }

    @Override
    public PisicaListRequest getAllByPisiciOrderedByVarsta(Pageable pageable) {
        Pageable effective = pageable == null ? Pageable.unpaged() : pageable;
        log.debug("Listare ordonata dupa varsta, page {}", effective.getPageNumber());
        Page<Pisica> pisicaPage = pisicaRepository.findAll(effective);
        List<PisicaDto>pisicaDtoList=pisicaManualMapper.mapperPisicaListToPisicaDtoList(pisicaPage.getContent());
        return new PisicaListRequest(pisicaDtoList);
    }
    public PisicaNumeVarstaListRequest findByRasaSortedByVarsta(String rasa, Pageable pageable) throws RasaNotFoundException {
        Pageable effective = pageable == null ? Pageable.unpaged() : pageable;
        Page<Pisica> pisicaPage = pisicaRepository.findByRasaOrderByVarstaAsc(rasa, effective);
        if (pisicaPage.isEmpty()) {
            log.warn("Nu exista pisici pentru rasa {}", rasa);
            throw new RasaNotFoundException();
        }
        List<PisicaNumeVarstaInfo> infoList = pisicaManualMapper.mapperPisicaListToNumeVarstaInfoList(pisicaPage.getContent());
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
    public PisicaNumeRasaListRequest findByNumeStartingWith(String nume, Pageable pageable)throws NoPisicaFoundException{
        Pageable effective = pageable == null ? Pageable.unpaged() : pageable;
        Page<Pisica> pisicaPage = pisicaRepository.findByNumeStartingWith(nume, effective);
        if(pisicaPage.isEmpty()){
            log.warn("Nu exista pisici cu nume care incepe cu {}", nume);
            throw new NoPisicaFoundException();
        }
        List<PisicaNumeRasaInfo> infoList=pisicaManualMapper.mapperPisicaListToNumeRasaInfoList(pisicaPage.getContent());
        return new PisicaNumeRasaListRequest(infoList);
    }

    @Override
    public PisicaIdNumeRasaListRequest findByVarstaExact(int varsta, Pageable pageable)throws NoPisicaFoundException{
        Pageable effective = pageable == null ? Pageable.unpaged() : pageable;
        Page<Pisica> pisicaPage=pisicaRepository.findByVarsta(varsta, effective);
        if(pisicaPage.isEmpty()){
            log.warn("Nu exista pisici cu varsta {}", varsta);
            throw new NoPisicaFoundException();
        }
        List<PisicaIdNumeRasaInfo> infoList=pisicaManualMapper.mapperPisicaListToIdNumeRasaInfoList(pisicaPage.getContent());
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
    public RasaAverageAgeListRequest findAverageAgeByRasa(Pageable pageable) throws NoResultsTopQueryException {
        Pageable effective = pageable == null ? Pageable.unpaged() : pageable;
        Page<RasaAverageAgeInfo> infoPage = pisicaRepository.findRasaAverageAge(effective);
        if(infoPage.isEmpty()){
            log.warn("Nu s-au putut calcula varste medii pe rasa");
            throw new NoResultsTopQueryException();
        }
        return new RasaAverageAgeListRequest(infoPage.getContent());
    }

}
