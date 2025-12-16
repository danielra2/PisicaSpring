package mycode.pisicaspring.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import mycode.pisicaspring.dtos.*;
import mycode.pisicaspring.service.PisicaCommandService;
import mycode.pisicaspring.service.PisicaQuerryService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class PisicaController implements PisicaControllerApi {

    private final PisicaCommandService pisicaCommandService;
    private final PisicaQuerryService pisicaQuerryService;

    public PisicaController(PisicaCommandService pisicaCommandService,
                            PisicaQuerryService pisicaQuerryService) {
        this.pisicaCommandService = pisicaCommandService;
        this.pisicaQuerryService = pisicaQuerryService;
    }

    @Override
    public PisicaListResponsePageable getAllPisici(@ParameterObject @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        log.info("GET /api/pisici requested: page={}, size={}, sort={}", pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());
        PisicaListResponsePageable response = pisicaQuerryService.getAllPisici(pageable);
        return response;
    }

    @Override
    public PisicaResponse createPisica(@Valid @RequestBody PisicaDto pisicaDto) {
        log.info("POST /api/pisici/add - creare pisica {} {}", pisicaDto.nume(), pisicaDto.rasa());
        return pisicaCommandService.createPisica(pisicaDto);
    }

    @Override
    public void deletePisica(@PathVariable String nume) {
        log.info("DELETE /api/pisici/delete/{}", nume);
        PisicaDto request = new PisicaDto("N/A", 0, nume);
        pisicaCommandService.deletePisicaByName(request);
    }

    @Override
    public PisicaResponse updatePisica(@PathVariable String currentName,
                                       @Valid @RequestBody PisicaDto updatedPisica) {
        log.info("PUT /api/pisici/{} - actualizare catre {}", currentName, updatedPisica.nume());
        return pisicaCommandService.updatePisicaByName(currentName, updatedPisica);
    }

    @Override
    public List<PisicaNumeVarstaDto> getPisiciMaiBatrane(@PathVariable int varstaMinima,
                                                         @ParameterObject @PageableDefault(size = 10) Pageable pageable) {
        List<PisicaNumeVarstaDto> paged = pisicaQuerryService.getOlderPisiciInfo(varstaMinima, pageable);
        log.info("GET older-than {} -> {} rezultate", varstaMinima, paged.size());
        return paged;
    }

    @Override
    public PisicaRasaListRequest getRaseUnice(@ParameterObject @PageableDefault(size = 10) Pageable pageable) {
        PisicaRasaListRequest response = pisicaQuerryService.findAllUniqueRase(pageable);
        log.info("GET rase/unice -> {} rase", response.pisicaRasaInfoList().size());
        return response;
    }

    @Override
    public PisicaVarstaRangeListRequest getPisiciByVarstaRange(@RequestParam int min,
                                                               @RequestParam int max,
                                                               @ParameterObject @PageableDefault(size = 10) Pageable pageable) {
        PisicaVarstaRangeListRequest response = pisicaQuerryService.findPisiciByVarstaRange(min, max, pageable);
        log.info("GET varsta?min={}&max={} -> {} rezultate", min, max, response.pisiciList().size());
        return response;
    }

    @Override
    public PisicaListRequest getOrderedByVarsta(@ParameterObject @PageableDefault(size = 10) Pageable pageable) {
        PisicaListRequest paged = pisicaQuerryService.getAllByPisiciOrderedByVarsta(pageable);
        log.info("GET ordered-by-varsta -> {} rezultate", paged.pisicaDtoList().size());
        return paged;
    }

    @Override
    public PisicaNumeVarstaListRequest getByRasaOrdered(@PathVariable String rasa,
                                                        @ParameterObject @PageableDefault(size = 10) Pageable pageable) {
        PisicaNumeVarstaListRequest paged = pisicaQuerryService.findByRasaSortedByVarsta(rasa, pageable);
        log.info("GET rasa/{}/ordered-by-varsta -> {} rezultate", rasa, paged.pisiciList().size());
        return paged;
    }

    @Override
    public PisicaListRequest getTop3Tinere() {
        return pisicaQuerryService.findTop3YoungestPisici();
    }

    @Override
    public PisicaNumeRasaListRequest getByNumePrefix(@PathVariable String prefix,
                                                     @ParameterObject @PageableDefault(size = 10) Pageable pageable) {
        PisicaNumeRasaListRequest paged = pisicaQuerryService.findByNumeStartingWith(prefix, pageable);
        log.info("GET nume/incepe-cu/{} -> {} rezultate", prefix, paged.pisiciList().size());
        return paged;
    }

    @Override
    public PisicaIdNumeRasaListRequest getByVarstaExacta(@PathVariable int varstaExacta,
                                                         @ParameterObject @PageableDefault(size = 10) Pageable pageable) {
        PisicaIdNumeRasaListRequest paged = pisicaQuerryService.findByVarstaExact(varstaExacta, pageable);
        log.info("GET varsta/{} -> {} rezultate", varstaExacta, paged.pisiciList().size());
        return paged;
    }

    @Override
    public PisicaListRequest getTop5Batrane() {
        return pisicaQuerryService.findTop5OldestPisici();
    }

    @Override
    public RasaAverageAgeListRequest getAverageAgeByRasa(@ParameterObject @PageableDefault(size = 10) Pageable pageable) {
        return pisicaQuerryService.findAverageAgeByRasa(pageable);
}
}
