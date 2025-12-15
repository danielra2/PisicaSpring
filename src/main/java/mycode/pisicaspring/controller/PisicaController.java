package mycode.pisicaspring.controller;

import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import mycode.pisicaspring.audit.QueryAuditService;
import mycode.pisicaspring.dtos.*;
import mycode.pisicaspring.service.PisicaCommandService;
import mycode.pisicaspring.service.PisicaQuerryService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pisici")
@Tag(name = "Pisici", description = "Operatii de gestionare a pisicilor")
@Slf4j
public class PisicaController {

    private final PisicaCommandService pisicaCommandService;
    private final PisicaQuerryService pisicaQuerryService;
    private final QueryAuditService queryAuditService;

    public PisicaController(PisicaCommandService pisicaCommandService,
                            PisicaQuerryService pisicaQuerryService,
                            QueryAuditService queryAuditService) {
        this.pisicaCommandService = pisicaCommandService;
        this.pisicaQuerryService = pisicaQuerryService;
        this.queryAuditService = queryAuditService;
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Lista pisicilor", description = "Returneaza pisicile in mod paginat")
    @Parameters({
            @Parameter(name = "page", description = "Indexul paginii (0 pentru prima pagina)", schema = @Schema(example = "0")),
            @Parameter(name = "size", description = "Numarul de elemente pe pagina", schema = @Schema(example = "10")),
            @Parameter(name = "sort", description = "Camp si directie. Exemplu: nume,asc", schema = @Schema(example = "nume,asc"))
    })
    public PisicaListResponsePageable getAllPisici(@ParameterObject @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        log.info("GET /api/pisici requested: page={}, size={}, sort={}", pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());
        PisicaListResponsePageable response = pisicaQuerryService.getAllPisici(pageable);
        queryAuditService.record("GET /api/pisici", Map.of(
                "page", pageable.getPageNumber(),
                "size", pageable.getPageSize(),
                "count", response.pisicaDtoList().size()
        ));
        return response;
    }

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Creeaza o pisica", description = "Adauga o pisica in baza de date")
    public PisicaResponse createPisica(@Valid @RequestBody PisicaDto pisicaDto) {
        log.info("POST /api/pisici/add - creare pisica {} {}", pisicaDto.nume(), pisicaDto.rasa());
        return pisicaCommandService.createPisica(pisicaDto);
    }

    @DeleteMapping("/delete/{nume}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Sterge o pisica", description = "Elimina o pisica din baza dupa nume")
    public void deletePisica(@PathVariable String nume) {
        log.info("DELETE /api/pisici/delete/{}", nume);
        PisicaDto request = new PisicaDto("N/A", 0, nume);
        pisicaCommandService.deletePisicaByName(request);
    }

    @PutMapping("/{currentName}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Actualizeaza o pisica", description = "Modifica o pisica identificata prin nume")
    public PisicaResponse updatePisica(@PathVariable String currentName,
                                       @Valid @RequestBody PisicaDto updatedPisica) {
        log.info("PUT /api/pisici/{} - actualizare catre {}", currentName, updatedPisica.nume());
        return pisicaCommandService.updatePisicaByName(currentName, updatedPisica);
    }

    @GetMapping("/older-than/{varstaMinima}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Pisici mai batrane", description = "Lista pisicilor cu varsta peste valoarea primita")
    @Parameters({
            @Parameter(name = "page", schema = @Schema(example = "0")),
            @Parameter(name = "size", schema = @Schema(example = "5")),
            @Parameter(name = "sort", description = "Ex. varsta,desc", schema = @Schema(example = "varsta,desc"))
    })
    public List<PisicaNumeVarstaDto> getPisiciMaiBatrane(@PathVariable int varstaMinima,
                                                         @ParameterObject @PageableDefault(size = 10) Pageable pageable) {
        List<PisicaNumeVarstaDto> paged = pisicaQuerryService.getOlderPisiciInfo(varstaMinima, pageable);
        log.info("GET older-than {} -> {} rezultate", varstaMinima, paged.size());
        queryAuditService.record("GET /api/pisici/older-than/{varstaMinima}", Map.of(
                "varstaMinima", varstaMinima,
                "page", pageable.getPageNumber(),
                "size", pageable.getPageSize(),
                "count", paged.size()
        ));
        return paged;
    }

    @GetMapping("/rase/unice")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Rase unice", description = "Afiseaza rasele distincte existente")
    @Parameters({
            @Parameter(name = "page", schema = @Schema(example = "0")),
            @Parameter(name = "size", schema = @Schema(example = "10")),
            @Parameter(name = "sort", description = "Ex. rasa,asc", schema = @Schema(example = "rasa,asc"))
    })
    public PisicaRasaListRequest getRaseUnice(@ParameterObject @PageableDefault(size = 10) Pageable pageable) {
        PisicaRasaListRequest response = pisicaQuerryService.findAllUniqueRase(pageable);
        log.info("GET rase/unice -> {} rase", response.pisicaRasaInfoList().size());
        queryAuditService.record("GET /api/pisici/rase/unice", Map.of(
                "page", pageable.getPageNumber(),
                "size", pageable.getPageSize(),
                "count", response.pisicaRasaInfoList().size()
        ));
        return response;
    }

    @GetMapping("/varsta")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Filtrare dupa interval de varsta", description = "Returneaza pisicile din intervalul ales")
    @Parameters({
            @Parameter(name = "page", schema = @Schema(example = "0")),
            @Parameter(name = "size", schema = @Schema(example = "10")),
            @Parameter(name = "sort", description = "Ex. varsta,asc", schema = @Schema(example = "varsta,asc"))
    })
    public PisicaVarstaRangeListRequest getPisiciByVarstaRange(@RequestParam int min,
                                                               @RequestParam int max,
                                                               @ParameterObject @PageableDefault(size = 10) Pageable pageable) {
        PisicaVarstaRangeListRequest response = pisicaQuerryService.findPisiciByVarstaRange(min, max, pageable);
        log.info("GET varsta?min={}&max={} -> {} rezultate", min, max, response.pisiciList().size());
        queryAuditService.record("GET /api/pisici/varsta", Map.of(
                "min", min,
                "max", max,
                "page", pageable.getPageNumber(),
                "size", pageable.getPageSize(),
                "count", response.pisiciList().size()
        ));
        return response;
    }

    @GetMapping("/ordered-by-varsta")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Pisici ordonate dupa varsta", description = "Listeaza pisicile ordonate crescator dupa varsta")
    @Parameters({
            @Parameter(name = "page", schema = @Schema(example = "0")),
            @Parameter(name = "size", schema = @Schema(example = "10")),
            @Parameter(name = "sort", description = "Ex. varsta,asc", schema = @Schema(example = "varsta,asc"))
    })
    public PisicaListRequest getOrderedByVarsta(@ParameterObject @PageableDefault(size = 10) Pageable pageable) {
        PisicaListRequest paged = pisicaQuerryService.getAllByPisiciOrderedByVarsta(pageable);
        log.info("GET ordered-by-varsta -> {} rezultate", paged.pisicaDtoList().size());
        queryAuditService.record("GET /api/pisici/ordered-by-varsta", Map.of(
                "page", pageable.getPageNumber(),
                "size", pageable.getPageSize(),
                "count", paged.pisicaDtoList().size()
        ));
        return paged;
    }

    @GetMapping("/rasa/{rasa}/ordered-by-varsta")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Pisici dupa rasa", description = "Listeaza pisicile dintr-o rasa ordonate dupa varsta")
    @Parameters({
            @Parameter(name = "page", schema = @Schema(example = "0")),
            @Parameter(name = "size", schema = @Schema(example = "10")),
            @Parameter(name = "sort", description = "Ex. varsta,asc", schema = @Schema(example = "varsta,asc"))
    })
    public PisicaNumeVarstaListRequest getByRasaOrdered(@PathVariable String rasa,
                                                        @ParameterObject @PageableDefault(size = 10) Pageable pageable) {
        PisicaNumeVarstaListRequest paged = pisicaQuerryService.findByRasaSortedByVarsta(rasa, pageable);
        log.info("GET rasa/{}/ordered-by-varsta -> {} rezultate", rasa, paged.pisiciList().size());
        queryAuditService.record("GET /api/pisici/rasa/{rasa}/ordered-by-varsta", Map.of(
                "rasa", rasa,
                "page", pageable.getPageNumber(),
                "size", pageable.getPageSize(),
                "count", paged.pisiciList().size()
        ));
        return paged;
    }

    @GetMapping("/top/tinere")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Top 3 cele mai tinere", description = "Returneaza cele mai tinere trei pisici")
    public PisicaListRequest getTop3Tinere() {
        PisicaListRequest response = pisicaQuerryService.findTop3YoungestPisici();
        queryAuditService.record("GET /api/pisici/top/tinere", Map.of(
                "count", response.pisicaDtoList().size()
        ));
        return response;
    }

    @GetMapping("/nume/incepe-cu/{prefix}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Cautare dupa prefix", description = "Returneaza pisicile al caror nume incepe cu prefixul dat")
    @Parameters({
            @Parameter(name = "page", schema = @Schema(example = "0")),
            @Parameter(name = "size", schema = @Schema(example = "10")),
            @Parameter(name = "sort", description = "Ex. nume,asc", schema = @Schema(example = "nume,asc"))
    })
    public PisicaNumeRasaListRequest getByNumePrefix(@PathVariable String prefix,
                                                     @ParameterObject @PageableDefault(size = 10) Pageable pageable) {
        PisicaNumeRasaListRequest paged = pisicaQuerryService.findByNumeStartingWith(prefix, pageable);
        log.info("GET nume/incepe-cu/{} -> {} rezultate", prefix, paged.pisiciList().size());
        queryAuditService.record("GET /api/pisici/nume/incepe-cu/{prefix}", Map.of(
                "prefix", prefix,
                "page", pageable.getPageNumber(),
                "size", pageable.getPageSize(),
                "count", paged.pisiciList().size()
        ));
        return paged;
    }

    @GetMapping("/varsta/{varstaExacta}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Cautare dupa varsta exacta", description = "Returneaza pisicile cu varsta exacta solicitata")
    @Parameters({
            @Parameter(name = "page", schema = @Schema(example = "0")),
            @Parameter(name = "size", schema = @Schema(example = "10")),
            @Parameter(name = "sort", description = "Ex. nume,asc", schema = @Schema(example = "nume,asc"))
    })
    public PisicaIdNumeRasaListRequest getByVarstaExacta(@PathVariable int varstaExacta,
                                                         @ParameterObject @PageableDefault(size = 10) Pageable pageable) {
        PisicaIdNumeRasaListRequest paged = pisicaQuerryService.findByVarstaExact(varstaExacta, pageable);
        log.info("GET varsta/{} -> {} rezultate", varstaExacta, paged.pisiciList().size());
        queryAuditService.record("GET /api/pisici/varsta/{varstaExacta}", Map.of(
                "varstaExacta", varstaExacta,
                "page", pageable.getPageNumber(),
                "size", pageable.getPageSize(),
                "count", paged.pisiciList().size()
        ));
        return paged;
    }

    @GetMapping("/top/batrane")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Top 5 cele mai batrane", description = "Returneaza cele mai batrane cinci pisici")
    public PisicaListRequest getTop5Batrane() {
        PisicaListRequest response = pisicaQuerryService.findTop5OldestPisici();
        queryAuditService.record("GET /api/pisici/top/batrane", Map.of(
                "count", response.pisicaDtoList().size()
        ));
        return response;
    }

    @GetMapping("/rase/varsta-medie")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Varsta medie pe rasa", description = "Returneaza media de varsta pentru fiecare rasa")
    @Parameters({
            @Parameter(name = "page", schema = @Schema(example = "0")),
            @Parameter(name = "size", schema = @Schema(example = "10")),
            @Parameter(name = "sort", description = "Ex. rasa,asc", schema = @Schema(example = "rasa,asc"))
    })
    public RasaAverageAgeListRequest getAverageAgeByRasa(@ParameterObject @PageableDefault(size = 10) Pageable pageable) {
        RasaAverageAgeListRequest paged = pisicaQuerryService.findAverageAgeByRasa(pageable);
        queryAuditService.record("GET /api/pisici/rase/varsta-medie", Map.of(
                "page", pageable.getPageNumber(),
                "size", pageable.getPageSize(),
                "count", paged.averageAgeList().size()
        ));
        return paged;
    }
}
