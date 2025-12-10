package mycode.pisicaspring.mappers;

import mycode.pisicaspring.dtos.*;
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

    public List<PisicaResponse>mapPisicaListToResponseList(List<Pisica>list){
        if(list==null)return List.of();
        return list.stream().map(this::mapPisicaToPisicaResponse).toList();
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

    public PisicaRasaInfo mapPisicaToRasaInfo(Pisica e) {
        Objects.requireNonNull(e, "entity is null");
        return new PisicaRasaInfo(
                nvl(e.getRasa())
        );
    }

    public List<PisicaRasaInfo> mapperPisicaListToRasaInfoList(List<Pisica> list) {
        if (list == null) return List.of();
        return list.stream()
                .filter(Objects::nonNull)
                .map(this::mapPisicaToRasaInfo)
                .distinct()
                .toList();
    }

    public PisicaVarstaRangeInfo mapPisicaToVarstaRangeInfo(Pisica e) {
        Objects.requireNonNull(e, "entity is null");
        return new PisicaVarstaRangeInfo(
                nvl(e.getNume()),
                nvl(e.getRasa()),
                e.getVarsta()
        );
    }
    public List<PisicaVarstaRangeInfo> mapperPisicaListToVarstaRangeInfoList(List<Pisica> list) {
        if (list == null) return List.of();
        // Aplicăm maparea 1-la-1
        return list.stream()
                .filter(Objects::nonNull)
                .map(this::mapPisicaToVarstaRangeInfo)
                .toList(); // Nu este necesar distinct aici
    }

    public PisicaNumeVarstaInfo mapPisicaToNumeVarstaInfo(Pisica e) {
        Objects.requireNonNull(e, "entity is null");
        return new PisicaNumeVarstaInfo(
                nvl(e.getNume()),
                e.getVarsta()
        );
    }

    public List<PisicaNumeVarstaInfo> mapperPisicaListToNumeVarstaInfoList(List<Pisica> list) {
        if (list == null) return List.of();
        // Folosim maparea 1-la-1 cerută
        return list.stream()
                .filter(Objects::nonNull)
                .map(this::mapPisicaToNumeVarstaInfo)
                .toList();
    }


    public PisicaNumeRasaInfo mapPisicaToNumeRasaInfo(Pisica e){
        Objects.requireNonNull(e,"entity is null");
        return new PisicaNumeRasaInfo(nvl(e.getNume()),nvl(e.getRasa()));
    }
    public List<PisicaNumeRasaInfo> mapperPisicaListToNumeRasaInfoList(List<Pisica> list){
        if(list==null)return List.of();
        return list.stream().filter(Objects::nonNull).map(this::mapPisicaToNumeRasaInfo).toList();
    }




    public PisicaIdNumeRasaInfo mapPisicaToIdNumeRasaInfo(Pisica e){
        Objects.requireNonNull(e,"entity is null");
        return new PisicaIdNumeRasaInfo(e.getId(),nvl(e.getNume()),nvl(e.getRasa()));
    }
    public List<PisicaIdNumeRasaInfo> mapperPisicaListToIdNumeRasaInfoList(List<Pisica> list){
        if(list==null)return List.of();
        return list.stream().filter(Objects::nonNull).map(this::mapPisicaToIdNumeRasaInfo).toList();
    }


    // Metode utilitare
    private static String trim(String s) {
        return s == null ? null : s.trim();
    }

    private static String nvl(String s) {
        return s == null ? "" : s;
    }
}