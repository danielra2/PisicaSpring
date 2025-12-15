package mycode.pisicaspring.service;

import jakarta.transaction.Transactional;
import mycode.pisicaspring.dtos.PisicaDto;
import mycode.pisicaspring.dtos.PisicaResponse;
import mycode.pisicaspring.exceptions.PisicaAlreadyExistsException;
import mycode.pisicaspring.exceptions.PisicaDoesntExistException;
import mycode.pisicaspring.mappers.PisicaManualMapper;
import mycode.pisicaspring.models.Pisica;
import mycode.pisicaspring.repository.PisicaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Component
@Service
@Slf4j
public class PisicaCommandServiceImpl implements PisicaCommandService {
    private PisicaRepository pisicaRepository;
    private PisicaManualMapper mapper;

    public PisicaCommandServiceImpl(PisicaRepository pisicaRepository,PisicaManualMapper pisicaManualMapper){
        this.pisicaRepository=pisicaRepository;
        this.mapper=pisicaManualMapper;
    }

    @Override
    public PisicaResponse createPisica(PisicaDto pisicaDto) throws PisicaAlreadyExistsException {
        Optional<Pisica>optionalPisica=pisicaRepository.findByNumeAndRasa(pisicaDto.nume(),pisicaDto.rasa());
        if(optionalPisica.isPresent()){
            log.warn("Incercare de creare a unei pisici deja existente: {} - {}", pisicaDto.nume(), pisicaDto.rasa());
           throw new PisicaAlreadyExistsException();
        }
        log.info("Creez pisica {} - {}", pisicaDto.nume(), pisicaDto.rasa());
        Pisica pisica=mapper.mapPisicaDtoToPisica(pisicaDto);
        pisica=this.pisicaRepository.save(pisica);
        return mapper.mapPisicaToPisicaResponse(pisica);
    }

    @Override
    @Transactional
    public PisicaResponse deletePisicaByName(PisicaDto pisicaDto) throws PisicaDoesntExistException {
        Optional<Pisica>pisicaOptional=pisicaRepository.findByNume(pisicaDto.nume());
        if(!pisicaOptional.isPresent()){
            log.warn("Incercare de stergere a unei pisici inexistente: {}", pisicaDto.nume());
            throw new PisicaDoesntExistException();
        }
        log.info("Sterg pisica {}", pisicaDto.nume());
        this.pisicaRepository.delete(pisicaOptional.get());
        return mapper.mapPisicaToPisicaResponse(pisicaOptional.get());

    }

    @Override
    @Transactional
    public PisicaResponse updatePisicaByName(String currentName, PisicaDto pisicaDto) throws PisicaDoesntExistException{
        Optional<Pisica>pisicaOptional=pisicaRepository.findByNume(currentName);
        if(!pisicaOptional.isPresent()){
            log.warn("Incercare de actualizare pentru pisica inexistenta: {}", currentName);
            throw new PisicaDoesntExistException();
        }
        log.info("Actualizez pisica {}", currentName);
        Pisica existingPisica=pisicaOptional.get();
        existingPisica.setNume(pisicaDto.nume());
        existingPisica.setRasa(pisicaDto.rasa());
        existingPisica.setVarsta(pisicaDto.varsta());

        existingPisica=pisicaRepository.save(existingPisica);

        return mapper.mapPisicaToPisicaResponse(existingPisica);

    }
}
