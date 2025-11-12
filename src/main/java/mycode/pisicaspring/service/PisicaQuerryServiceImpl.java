package mycode.pisicaspring.service;

import mycode.pisicaspring.dtos.PisicaListRequest;
import mycode.pisicaspring.mappers.PisicaManualMapper;
import mycode.pisicaspring.models.Pisica;
import mycode.pisicaspring.repository.PisicaRepository;
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
    public PisicaListRequest getAllPisici() {
        List<Pisica> pisicaList=pisicaRepository.getAllPisica();
        return new PisicaListRequest(this.pisicaManualMapper.mapperPisicaListToPisicaDtoList(pisicaList));
    }
}
