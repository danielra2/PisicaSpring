package mycode.pisicaspring.mappers;

import lombok.Builder;
import lombok.Getter;
import mycode.pisicaspring.dtos.PisicaDto;
import mycode.pisicaspring.dtos.PisicaResponse;
import mycode.pisicaspring.models.Pisica;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class PisicaManualMapper {


     //mapeaza de la PisicaDto la entitatea Pisica

    public Pisica mapPisicaDtoToPisica(PisicaDto dto) {
        Objects.requireNonNull(dto, "dto is null");
        var e = new Pisica();
        e.setRasa(trim(dto.rasa()));
        e.setVarsta(dto.varsta());
        e.setNume(trim(dto.nume()));
        return e;
    }


     // Mapeaza de la entitatea Pisica la PisicaDtoa

    public PisicaDto mapPisicaToPisicaDto(Pisica e) {
        Objects.requireNonNull(e, "entity is null");
        return new PisicaDto(
                nvl(e.getRasa()),
                e.getVarsta(),
                nvl(e.getNume())
        );
    }


    //  Mapeaza de la entitatea Pisica la PisicaResponsea

    public PisicaResponse mapPisicaToPisicaResponse(Pisica e) {
        Objects.requireNonNull(e, "entity is null");
        return new PisicaResponse(
                e.getId(),
                nvl(e.getRasa()),
                e.getVarsta(),
                nvl(e.getNume())
        );
    }


      //Mapeaza o listă de DTO la o lista de entitati Pisica.

    public List<Pisica> mapPisiciDtoListToPisicaList(List<PisicaDto> list) {
        if (list == null) return List.of();
        return list.stream().filter(Objects::nonNull).map(this::mapPisicaDtoToPisica).toList();
    }


     // Mapeaza o lista de entitati Pisica la o lista de DTO-uri.

    public List<PisicaDto> mapperPisicaListToPisicaDtoList(List<Pisica> list) {
        if (list == null) return List.of();
        return list.stream().filter(Objects::nonNull).map(this::mapPisicaToPisicaDto).toList();
    }


    // Metode utilitare
    private static String trim(String s) {
        return s == null ? null : s.trim();
    }

    private static String nvl(String s) {
        return s == null ? "" : s;
    }
}