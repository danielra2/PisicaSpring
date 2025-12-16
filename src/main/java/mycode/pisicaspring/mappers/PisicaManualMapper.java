package mycode.pisicaspring.mappers;

import mycode.pisicaspring.dtos.*;
import mycode.pisicaspring.models.Pisica;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

@Component
public class PisicaManualMapper {

    // Mapeaza de la PisicaDto la entitatea Pisica
    public Pisica mapPisicaDtoToPisica(PisicaDto dto) {
        Objects.requireNonNull(dto, "dto is null");
        var entity = new Pisica();
        updatePisicaFromDto(dto, entity);
        return entity;
    }

    public void updatePisicaFromDto(PisicaDto dto, Pisica target) {
        Objects.requireNonNull(dto, "dto is null");
        Objects.requireNonNull(target, "target is null");
        target.setRasa(trim(dto.rasa()));
        target.setVarsta(dto.varsta());
        target.setNume(trim(dto.nume()));
    }

    // Mapeaza de la entitatea Pisica la PisicaDto
    public PisicaDto mapPisicaToPisicaDto(Pisica e) {
        Objects.requireNonNull(e, "entity is null");
        return new PisicaDto(
                cleanText(e.getRasa()),
                e.getVarsta(),
                cleanText(e.getNume())
        );
    }

    //  Mapeaza de la entitatea Pisica la PisicaResponse
    public PisicaResponse mapPisicaToPisicaResponse(Pisica e) {
        Objects.requireNonNull(e, "entity is null");
        return new PisicaResponse(
                e.getId(),
                cleanText(e.getRasa()),
                e.getVarsta(),
                cleanText(e.getNume())
        );
    }

    public List<PisicaResponse> mapPisicaListToResponseList(List<Pisica> list) {
        return mapList(list, this::mapPisicaToPisicaResponse);
    }

    //Mapeaza o listă de DTO la o lista de entitati Pisica.
    public List<Pisica> mapPisiciDtoListToPisicaList(List<PisicaDto> list) {
        return mapList(list, this::mapPisicaDtoToPisica);
    }

    // Mapeaza o lista de entitati Pisica la o lista de DTO-uri.
    public List<PisicaDto> mapperPisicaListToPisicaDtoList(List<Pisica> list) {
        return mapList(list, this::mapPisicaToPisicaDto);
    }

    public PisicaRasaInfo mapPisicaToRasaInfo(Pisica e) {
        Objects.requireNonNull(e, "entity is null");
        return new PisicaRasaInfo(cleanText(e.getRasa()));
    }

    public List<PisicaRasaInfo> mapperPisicaListToRasaInfoList(List<Pisica> list) {
        return mapList(list, this::mapPisicaToRasaInfo).stream()
                .distinct()
                .toList();
    }

    public List<PisicaRasaInfo> mapRasaListToRasaInfoList(List<String> rase) {
        return mapList(rase, rasa -> new PisicaRasaInfo(cleanText(rasa))).stream()
                .distinct()
                .toList();
    }

    public PisicaVarstaRangeInfo mapPisicaToVarstaRangeInfo(Pisica e) {
        Objects.requireNonNull(e, "entity is null");
        return new PisicaVarstaRangeInfo(
                cleanText(e.getNume()),
                cleanText(e.getRasa()),
                e.getVarsta()
        );
    }

    public List<PisicaVarstaRangeInfo> mapperPisicaListToVarstaRangeInfoList(List<Pisica> list) {
        return mapList(list, this::mapPisicaToVarstaRangeInfo);
    }

    public PisicaNumeVarstaInfo mapPisicaToNumeVarstaInfo(Pisica e) {
        Objects.requireNonNull(e, "entity is null");
        return new PisicaNumeVarstaInfo(
                cleanText(e.getNume()),
                e.getVarsta()
        );
    }

    public List<PisicaNumeVarstaInfo> mapperPisicaListToNumeVarstaInfoList(List<Pisica> list) {
        return mapList(list, this::mapPisicaToNumeVarstaInfo);
    }

    public PisicaNumeRasaInfo mapPisicaToNumeRasaInfo(Pisica e) {
        Objects.requireNonNull(e, "entity is null");
        return new PisicaNumeRasaInfo(cleanText(e.getNume()), cleanText(e.getRasa()));
    }

    public List<PisicaNumeRasaInfo> mapperPisicaListToNumeRasaInfoList(List<Pisica> list) {
        return mapList(list, this::mapPisicaToNumeRasaInfo);
    }

    public PisicaIdNumeRasaInfo mapPisicaToIdNumeRasaInfo(Pisica e) {
        Objects.requireNonNull(e, "entity is null");
        return new PisicaIdNumeRasaInfo(e.getId(), cleanText(e.getNume()), cleanText(e.getRasa()));
    }

    public List<PisicaIdNumeRasaInfo> mapperPisicaListToIdNumeRasaInfoList(List<Pisica> list) {
        return mapList(list, this::mapPisicaToIdNumeRasaInfo);
    }

    public PisicaNumeVarstaDto mapPisicaToPisicaNumeVarstaDto(Pisica e) {
        Objects.requireNonNull(e, "entity is null");
        return new PisicaNumeVarstaDto(cleanText(e.getNume()), e.getVarsta());
    }

    public List<PisicaNumeVarstaDto> mapPisicaListToPisicaNumeVarstaDtoList(List<Pisica> list) {
        return mapList(list, this::mapPisicaToPisicaNumeVarstaDto);
    }

    // Metode utilitare
    private static String trim(String s) {
        return s == null ? null : s.trim();
    }

    private static String nvl(String s) {
        return s == null ? "" : s;
    }

    private static String cleanText(String s) {
        return nvl(trim(s));
    }

    private static <S, T> List<T> mapList(Collection<S> list, Function<S, T> mapper) {
        if (list == null || list.isEmpty()) {
            return List.of();
        }
        return list.stream()
                .filter(Objects::nonNull)
                .map(mapper)
                .toList();
    }
}
