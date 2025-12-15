package mycode.pisicaspring.controller;

import jakarta.validation.Valid;
import mycode.pisicaspring.dtos.*;
import mycode.pisicaspring.service.PisicaCommandService;
import mycode.pisicaspring.service.PisicaQuerryService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pisici")
public class PisicaController {

    private final PisicaCommandService pisicaCommandService;
    private final PisicaQuerryService pisicaQuerryService;

    public PisicaController(PisicaCommandService pisicaCommandService, PisicaQuerryService pisicaQuerryService) {
        this.pisicaCommandService = pisicaCommandService;
        this.pisicaQuerryService = pisicaQuerryService;
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public PisicaListRequest getAllPisici() {
        return pisicaQuerryService.getAllPisici();
    }

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public PisicaResponse createPisica(@Valid @RequestBody PisicaDto pisicaDto) {
        return pisicaCommandService.createPisica(pisicaDto);
    }

    @DeleteMapping("/delete/{nume}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePisica(@PathVariable String nume) {
        PisicaDto request = new PisicaDto("N/A", 0, nume);
        pisicaCommandService.deletePisicaByName(request);
    }

    @PutMapping("/{currentName}")
    @ResponseStatus(HttpStatus.OK)
    public PisicaResponse updatePisica(@PathVariable String currentName,
                                       @Valid @RequestBody PisicaDto updatedPisica) {
        return pisicaCommandService.updatePisicaByName(currentName, updatedPisica);
    }

    @GetMapping("/older-than/{varstaMinima}")
    @ResponseStatus(HttpStatus.OK)
    public List<PisicaNumeVarstaDto> getPisiciMaiBatrane(@PathVariable int varstaMinima) {
        return pisicaQuerryService.getOlderPisiciInfo(varstaMinima);
    }

    @GetMapping("/rase/unice")
    @ResponseStatus(HttpStatus.OK)
    public PisicaRasaListRequest getRaseUnice() {
        return pisicaQuerryService.findAllUniqueRase();
    }

    @GetMapping("/varsta")
    @ResponseStatus(HttpStatus.OK)
    public PisicaVarstaRangeListRequest getPisiciByVarstaRange(@RequestParam int min,
                                                               @RequestParam int max) {
        return pisicaQuerryService.findPisiciByVarstaRange(min, max);
    }

    @GetMapping("/ordered-by-varsta")
    @ResponseStatus(HttpStatus.OK)
    public PisicaListRequest getOrderedByVarsta() {
        return pisicaQuerryService.getAllByPisiciOrderedByVarsta();
    }

    @GetMapping("/rasa/{rasa}/ordered-by-varsta")
    @ResponseStatus(HttpStatus.OK)
    public PisicaNumeVarstaListRequest getByRasaOrdered(@PathVariable String rasa) {
        return pisicaQuerryService.findByRasaSortedByVarsta(rasa);
    }

    @GetMapping("/top/tinere")
    @ResponseStatus(HttpStatus.OK)
    public PisicaListRequest getTop3Tinere() {
        return pisicaQuerryService.findTop3YoungestPisici();
    }

    @GetMapping("/nume/incepe-cu/{prefix}")
    @ResponseStatus(HttpStatus.OK)
    public PisicaNumeRasaListRequest getByNumePrefix(@PathVariable String prefix) {
        return pisicaQuerryService.findByNumeStartingWith(prefix);
    }

    @GetMapping("/varsta/{varstaExacta}")
    @ResponseStatus(HttpStatus.OK)
    public PisicaIdNumeRasaListRequest getByVarstaExacta(@PathVariable int varstaExacta) {
        return pisicaQuerryService.findByVarstaExact(varstaExacta);
    }

    @GetMapping("/top/batrane")
    @ResponseStatus(HttpStatus.OK)
    public PisicaListRequest getTop5Batrane() {
        return pisicaQuerryService.findTop5OldestPisici();
    }

    @GetMapping("/rase/varsta-medie")
    @ResponseStatus(HttpStatus.OK)
    public RasaAverageAgeListRequest getAverageAgeByRasa() {
        return pisicaQuerryService.findAverageAgeByRasa();
    }
}
