package mycode.pisicaspring.service;

import mycode.pisicaspring.dtos.PisicaDto;
import mycode.pisicaspring.dtos.PisicaResponse;
import mycode.pisicaspring.exceptions.PisicaAlreadyExistsException;
import mycode.pisicaspring.exceptions.PisicaDoesntExistException;
import mycode.pisicaspring.mappers.PisicaManualMapper;
import mycode.pisicaspring.models.Pisica;
import mycode.pisicaspring.repository.PisicaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PisicaCommandServiceImplTest {

    @Mock
    private PisicaRepository repository;
    @Mock
    private PisicaManualMapper mapper;

    private PisicaCommandServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new PisicaCommandServiceImpl(repository, mapper);
    }

    @Test
    void createPisicaThrowsWhenEntityAlreadyExists() {
        PisicaDto dto = new PisicaDto("Siamese", 2, "Luna");
        when(repository.findByNumeAndRasa(dto.nume(), dto.rasa())).thenReturn(Optional.of(new Pisica()));

        assertThatThrownBy(() -> service.createPisica(dto))
                .isInstanceOf(PisicaAlreadyExistsException.class);

        verify(repository, never()).save(any());
    }

    @Test
    void createPisicaPersistsMappedEntity() {
        PisicaDto dto = new PisicaDto("Siamese", 2, "Luna");
        Pisica entity = new Pisica();
        PisicaResponse response = new PisicaResponse(1L, dto.rasa(), dto.varsta(), dto.nume());

        when(repository.findByNumeAndRasa(dto.nume(), dto.rasa())).thenReturn(Optional.empty());
        when(mapper.mapPisicaDtoToPisica(dto)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.mapPisicaToPisicaResponse(entity)).thenReturn(response);

        PisicaResponse result = service.createPisica(dto);

        assertThat(result).isEqualTo(response);
        verify(repository).save(entity);
    }

    @Test
    void deletePisicaThrowsWhenNotFound() {
        PisicaDto dto = new PisicaDto("Siamese", 3, "Luna");
        when(repository.findByNume(dto.nume())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.deletePisicaByName(dto))
                .isInstanceOf(PisicaDoesntExistException.class);

        verify(repository, never()).delete(any());
    }

    @Test
    void deletePisicaDeletesAndReturnsResponse() {
        PisicaDto dto = new PisicaDto("Siamese", 3, "Luna");
        Pisica entity = new Pisica();
        PisicaResponse response = new PisicaResponse(5L, dto.rasa(), dto.varsta(), dto.nume());
        when(repository.findByNume(dto.nume())).thenReturn(Optional.of(entity));
        when(mapper.mapPisicaToPisicaResponse(entity)).thenReturn(response);

        PisicaResponse result = service.deletePisicaByName(dto);

        assertThat(result).isEqualTo(response);
        verify(repository).delete(entity);
    }

    @Test
    void updatePisicaThrowsWhenEntityMissing() {
        PisicaDto dto = new PisicaDto("Siamese", 2, "Luna");
        when(repository.findByNume("Luna")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.updatePisicaByName("Luna", dto))
                .isInstanceOf(PisicaDoesntExistException.class);
    }

    @Test
    void updatePisicaDelegatesToMapperAndSaves() {
        PisicaDto dto = new PisicaDto("Siamese", 2, "Luna");
        Pisica entity = new Pisica();
        PisicaResponse response = new PisicaResponse(10L, dto.rasa(), dto.varsta(), dto.nume());

        when(repository.findByNume("Luna")).thenReturn(Optional.of(entity));
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.mapPisicaToPisicaResponse(entity)).thenReturn(response);

        PisicaResponse result = service.updatePisicaByName("Luna", dto);

        assertThat(result).isEqualTo(response);
        verify(mapper).updatePisicaFromDto(dto, entity);
        verify(repository).save(entity);
    }
}
