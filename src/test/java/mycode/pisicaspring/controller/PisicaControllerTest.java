package mycode.pisicaspring.controller;

import mycode.pisicaspring.dtos.*;
import mycode.pisicaspring.service.PisicaCommandService;
import mycode.pisicaspring.service.PisicaQuerryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PisicaControllerTest {

    @Mock
    private PisicaCommandService commandService;
    @Mock
    private PisicaQuerryService querryService;

    private PisicaController controller;
    private Pageable pageable;
    private PisicaDto dto;
    private PisicaResponse response;

    @BeforeEach
    void setUp() {
        controller = new PisicaController(commandService, querryService);
        pageable = PageRequest.of(0, 5);
        dto = new PisicaDto("Siamese", 2, "Luna");
        response = new PisicaResponse(1L, dto.rasa(), dto.varsta(), dto.nume());
    }

    @Test
    void getAllPisiciDelegatesToService() {
        PisicaListResponsePageable expected = new PisicaListResponsePageable(List.of(response), 1, 1, 0, 5);
        when(querryService.getAllPisici(pageable)).thenReturn(expected);

        PisicaListResponsePageable actual = controller.getAllPisici(pageable);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void createPisicaReturnsServiceResponse() {
        when(commandService.createPisica(dto)).thenReturn(response);
        assertThat(controller.createPisica(dto)).isEqualTo(response);
    }

    @Test
    void deletePisicaBuildsDto() {
        controller.deletePisica("Luna");

        ArgumentCaptor<PisicaDto> captor = ArgumentCaptor.forClass(PisicaDto.class);
        verify(commandService).deletePisicaByName(captor.capture());
        assertThat(captor.getValue().nume()).isEqualTo("Luna");
    }

    @Test
    void updatePisicaCallsCommandService() {
        when(commandService.updatePisicaByName("Luna", dto)).thenReturn(response);
        assertThat(controller.updatePisica("Luna", dto)).isEqualTo(response);
    }

    @Test
    void getPisiciMaiBatraneReturnsList() {
        List<PisicaNumeVarstaDto> expected = List.of(new PisicaNumeVarstaDto("Luna", 4));
        when(querryService.getOlderPisiciInfo(4, pageable)).thenReturn(expected);

        assertThat(controller.getPisiciMaiBatrane(4, pageable)).isEqualTo(expected);
    }

    @Test
    void getRaseUniceReturnsWrapper() {
        PisicaRasaListRequest expected = new PisicaRasaListRequest(List.of(new PisicaRasaInfo("Siamese")));
        when(querryService.findAllUniqueRase(pageable)).thenReturn(expected);

        assertThat(controller.getRaseUnice(pageable)).isEqualTo(expected);
    }

    @Test
    void getPisiciByVarstaRangeReturnsData() {
        PisicaVarstaRangeListRequest expected = new PisicaVarstaRangeListRequest(List.of(new PisicaVarstaRangeInfo("Luna", "Siamese", 5)));
        when(querryService.findPisiciByVarstaRange(1, 5, pageable)).thenReturn(expected);

        assertThat(controller.getPisiciByVarstaRange(1, 5, pageable)).isEqualTo(expected);
    }

    @Test
    void getOrderedByVarstaReturnsList() {
        PisicaListRequest expected = new PisicaListRequest(List.of(dto));
        when(querryService.getAllByPisiciOrderedByVarsta(pageable)).thenReturn(expected);

        assertThat(controller.getOrderedByVarsta(pageable)).isEqualTo(expected);
    }

    @Test
    void getByRasaOrderedReturnsData() throws Exception {
        PisicaNumeVarstaListRequest expected = new PisicaNumeVarstaListRequest(List.of(new PisicaNumeVarstaInfo("Luna", 2)));
        when(querryService.findByRasaSortedByVarsta("Siamese", pageable)).thenReturn(expected);

        assertThat(controller.getByRasaOrdered("Siamese", pageable)).isEqualTo(expected);
    }

    @Test
    void getTop3TinereAsksService() throws Exception {
        PisicaListRequest expected = new PisicaListRequest(List.of(dto));
        when(querryService.findTop3YoungestPisici()).thenReturn(expected);

        assertThat(controller.getTop3Tinere()).isEqualTo(expected);
    }

    @Test
    void getByNumePrefixReturnsList() throws Exception {
        PisicaNumeRasaListRequest expected = new PisicaNumeRasaListRequest(List.of(new PisicaNumeRasaInfo("Luna", "Siamese")));
        when(querryService.findByNumeStartingWith("Lu", pageable)).thenReturn(expected);

        assertThat(controller.getByNumePrefix("Lu", pageable)).isEqualTo(expected);
    }

    @Test
    void getByVarstaExactaReturnsList() throws Exception {
        PisicaIdNumeRasaListRequest expected = new PisicaIdNumeRasaListRequest(List.of(new PisicaIdNumeRasaInfo(1L, "Luna", "Siamese")));
        when(querryService.findByVarstaExact(3, pageable)).thenReturn(expected);

        assertThat(controller.getByVarstaExacta(3, pageable)).isEqualTo(expected);
    }

    @Test
    void getTop5BatraneReturnsList() throws Exception {
        PisicaListRequest expected = new PisicaListRequest(List.of(dto));
        when(querryService.findTop5OldestPisici()).thenReturn(expected);

        assertThat(controller.getTop5Batrane()).isEqualTo(expected);
    }

    @Test
    void getAverageAgeByRasaReturnsStats() throws Exception {
        RasaAverageAgeListRequest expected = new RasaAverageAgeListRequest(List.of(new RasaAverageAgeInfo("Siamese", 4.5)));
        when(querryService.findAverageAgeByRasa(pageable)).thenReturn(expected);

        assertThat(controller.getAverageAgeByRasa(pageable)).isEqualTo(expected);
    }
}
