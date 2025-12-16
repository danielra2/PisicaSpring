package mycode.pisicaspring.service;

import mycode.pisicaspring.dtos.*;
import mycode.pisicaspring.exceptions.*;
import mycode.pisicaspring.mappers.PisicaManualMapper;
import mycode.pisicaspring.models.Pisica;
import mycode.pisicaspring.repository.PisicaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PisicaQuerryServiceImplTest {

    @Mock
    private PisicaRepository repository;
    @Mock
    private PisicaManualMapper mapper;

    private PisicaQuerryServiceImpl service;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        service = new PisicaQuerryServiceImpl(repository, mapper);
        pageable = PageRequest.of(0, 5);
    }

    private Pisica pisica() {
        Pisica p = new Pisica();
        p.setId(1L);
        p.setNume("Luna");
        p.setRasa("Siamese");
        p.setVarsta(2);
        return p;
    }

    @Test
    void getAllPisiciReturnsMappedPage() {
        Page<Pisica> page = new PageImpl<>(List.of(pisica()), pageable, 1);
        List<PisicaResponse> responses = List.of(new PisicaResponse(1L, "Siamese", 2, "Luna"));
        when(repository.findAll(pageable)).thenReturn(page);
        when(mapper.mapPisicaListToResponseList(page.getContent())).thenReturn(responses);

        PisicaListResponsePageable result = service.getAllPisici(pageable);

        assertThat(result.pisicaDtoList()).isEqualTo(responses);
        assertThat(result.totalElements()).isEqualTo(1);
    }

    @Test
    void getOlderPisiciInfoDelegatesToMapper() {
        Page<Pisica> page = new PageImpl<>(List.of(pisica()));
        List<PisicaNumeVarstaDto> dtos = List.of(new PisicaNumeVarstaDto("Luna", 2));
        when(repository.findByVarstaGreaterThan(3, pageable)).thenReturn(page);
        when(mapper.mapPisicaListToPisicaNumeVarstaDtoList(page.getContent())).thenReturn(dtos);

        assertThat(service.getOlderPisiciInfo(3, pageable)).isEqualTo(dtos);
    }

    @Test
    void findAllUniqueRaseThrowsWhenEmpty() {
        when(repository.findDistinctRase(pageable)).thenReturn(new PageImpl<>(List.of(), pageable, 0));

        assertThatThrownBy(() -> service.findAllUniqueRase(pageable))
                .isInstanceOf(RasaIsEmpty.class);
    }

    @Test
    void findAllUniqueRaseReturnsMappedList() throws RasaIsEmpty {
        Page<String> page = new PageImpl<>(List.of("Siamese"), pageable, 1);
        List<PisicaRasaInfo> infos = List.of(new PisicaRasaInfo("Siamese"));
        when(repository.findDistinctRase(pageable)).thenReturn(page);
        when(mapper.mapRasaListToRasaInfoList(page.getContent())).thenReturn(infos);

        PisicaRasaListRequest result = service.findAllUniqueRase(pageable);
        assertThat(result.pisicaRasaInfoList()).isEqualTo(infos);
    }

    @Test
    void findPisiciByVarstaRangeReturnsInfoList() {
        Page<Pisica> page = new PageImpl<>(List.of(pisica()));
        List<PisicaVarstaRangeInfo> infos = List.of(new PisicaVarstaRangeInfo("Luna", "Siamese", 2));
        when(repository.findByVarstaBetween(1, 5, pageable)).thenReturn(page);
        when(mapper.mapperPisicaListToVarstaRangeInfoList(page.getContent())).thenReturn(infos);

        PisicaVarstaRangeListRequest result = service.findPisiciByVarstaRange(1, 5, pageable);
        assertThat(result.pisiciList()).isEqualTo(infos);
    }

    @Test
    void getAllByPisiciOrderedByVarstaUsesMapper() {
        Page<Pisica> page = new PageImpl<>(List.of(pisica()));
        List<PisicaDto> dtos = List.of(new PisicaDto("Siamese", 2, "Luna"));
        when(repository.findAll(pageable)).thenReturn(page);
        when(mapper.mapperPisicaListToPisicaDtoList(page.getContent())).thenReturn(dtos);

        PisicaListRequest result = service.getAllByPisiciOrderedByVarsta(pageable);
        assertThat(result.pisicaDtoList()).isEqualTo(dtos);
    }

    @Test
    void findByRasaSortedByVarstaThrowsWhenEmpty() {
        when(repository.findByRasaOrderByVarstaAsc(any(), any())).thenReturn(new PageImpl<>(List.of()));

        assertThatThrownBy(() -> service.findByRasaSortedByVarsta("Siamese", pageable))
                .isInstanceOf(RasaNotFoundException.class);
    }

    @Test
    void findByRasaSortedByVarstaReturnsList() throws RasaNotFoundException {
        Page<Pisica> page = new PageImpl<>(List.of(pisica()));
        List<PisicaNumeVarstaInfo> infos = List.of(new PisicaNumeVarstaInfo("Luna", 2));
        when(repository.findByRasaOrderByVarstaAsc("Siamese", pageable)).thenReturn(page);
        when(mapper.mapperPisicaListToNumeVarstaInfoList(page.getContent())).thenReturn(infos);

        PisicaNumeVarstaListRequest result = service.findByRasaSortedByVarsta("Siamese", pageable);
        assertThat(result.pisiciList()).isEqualTo(infos);
    }

    @Test
    void findTop3YoungestPisiciThrowsWhenEmpty() {
        when(repository.findTop3ByOrderByVarstaAsc()).thenReturn(List.of());

        assertThatThrownBy(() -> service.findTop3YoungestPisici())
                .isInstanceOf(PisicaDoesntExistException.class);
    }

    @Test
    void findTop3YoungestPisiciReturnsList() throws PisicaDoesntExistException {
        List<Pisica> entities = List.of(pisica());
        List<PisicaDto> dtos = List.of(new PisicaDto("Siamese", 2, "Luna"));
        when(repository.findTop3ByOrderByVarstaAsc()).thenReturn(entities);
        when(mapper.mapperPisicaListToPisicaDtoList(entities)).thenReturn(dtos);

        PisicaListRequest result = service.findTop3YoungestPisici();
        assertThat(result.pisicaDtoList()).isEqualTo(dtos);
    }

    @Test
    void findByNumeStartingWithThrowsWhenEmpty() {
        when(repository.findByNumeStartingWith(any(), any())).thenReturn(new PageImpl<>(List.of()));

        assertThatThrownBy(() -> service.findByNumeStartingWith("Lu", pageable))
                .isInstanceOf(NoPisicaFoundException.class);
    }

    @Test
    void findByNumeStartingWithReturnsList() throws NoPisicaFoundException {
        Page<Pisica> page = new PageImpl<>(List.of(pisica()));
        List<PisicaNumeRasaInfo> infos = List.of(new PisicaNumeRasaInfo("Luna", "Siamese"));
        when(repository.findByNumeStartingWith("Lu", pageable)).thenReturn(page);
        when(mapper.mapperPisicaListToNumeRasaInfoList(page.getContent())).thenReturn(infos);

        PisicaNumeRasaListRequest result = service.findByNumeStartingWith("Lu", pageable);
        assertThat(result.pisiciList()).isEqualTo(infos);
    }

    @Test
    void findByVarstaExactThrowsWhenEmpty() {
        when(repository.findByVarsta(anyInt(), any())).thenReturn(new PageImpl<>(List.of()));

        assertThatThrownBy(() -> service.findByVarstaExact(3, pageable))
                .isInstanceOf(NoPisicaFoundException.class);
    }

    @Test
    void findByVarstaExactReturnsList() throws NoPisicaFoundException {
        Page<Pisica> page = new PageImpl<>(List.of(pisica()));
        List<PisicaIdNumeRasaInfo> infos = List.of(new PisicaIdNumeRasaInfo(1L, "Luna", "Siamese"));
        when(repository.findByVarsta(3, pageable)).thenReturn(page);
        when(mapper.mapperPisicaListToIdNumeRasaInfoList(page.getContent())).thenReturn(infos);

        PisicaIdNumeRasaListRequest result = service.findByVarstaExact(3, pageable);
        assertThat(result.pisiciList()).isEqualTo(infos);
    }

    @Test
    void findTop5OldestPisiciThrowsWhenEmpty() {
        when(repository.findTop5ByOrderByVarstaDesc()).thenReturn(List.of());

        assertThatThrownBy(() -> service.findTop5OldestPisici())
                .isInstanceOf(NoResultsTopQueryException.class);
    }

    @Test
    void findTop5OldestPisiciReturnsList() throws NoResultsTopQueryException {
        List<Pisica> entities = List.of(pisica());
        List<PisicaDto> dtos = List.of(new PisicaDto("Siamese", 2, "Luna"));
        when(repository.findTop5ByOrderByVarstaDesc()).thenReturn(entities);
        when(mapper.mapperPisicaListToPisicaDtoList(entities)).thenReturn(dtos);

        PisicaListRequest result = service.findTop5OldestPisici();
        assertThat(result.pisicaDtoList()).isEqualTo(dtos);
    }

    @Test
    void findAverageAgeByRasaThrowsWhenEmpty() {
        when(repository.findRasaAverageAge(pageable)).thenReturn(new PageImpl<>(List.of(), pageable, 0));

        assertThatThrownBy(() -> service.findAverageAgeByRasa(pageable))
                .isInstanceOf(NoResultsTopQueryException.class);
    }

    @Test
    void findAverageAgeByRasaReturnsData() throws NoResultsTopQueryException {
        List<RasaAverageAgeInfo> infos = List.of(new RasaAverageAgeInfo("Siamese", 3.5));
        when(repository.findRasaAverageAge(pageable)).thenReturn(new PageImpl<>(infos, pageable, infos.size()));

        RasaAverageAgeListRequest result = service.findAverageAgeByRasa(pageable);
        assertThat(result.averageAgeList()).isEqualTo(infos);
    }
}
