package mycode.pisicaspring.service;

import mycode.pisicaspring.dtos.PisicaDto;
import mycode.pisicaspring.dtos.PisicaResponse;
import mycode.pisicaspring.mappers.PisicaManualMapper;
import mycode.pisicaspring.models.Pisica;
import mycode.pisicaspring.repository.PisicaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Component
@Service
public class PisicaCommandServiceImpl implements PisicaCommandService {
    private PisicaRepository pisicaRepository;
    private PisicaManualMapper mapper;

    public PisicaCommandServiceImpl(PisicaRepository pisicaRepository,PisicaManualMapper pisicaManualMapper){
        this.pisicaRepository=pisicaRepository;
        this.mapper=pisicaManualMapper;
    }

    @Override
    public PisicaResponse createPisica(PisicaDto pisicaDto) {
        Optional<Pisica>optionalPisica=pisicaRepository.findByNumeAndRasa(pisicaDto.nume(),pisicaDto.rasa());
        if(optionalPisica.isPresent()){
            System.out.println("Pisica exista deja");
        }
        Pisica pisica=mapper.mapPisicaDtoToPisica(pisicaDto);
        pisica=this.pisicaRepository.save(pisica);
        return mapper.mapPisicaToPisicaResponse(pisica);
    }
}
