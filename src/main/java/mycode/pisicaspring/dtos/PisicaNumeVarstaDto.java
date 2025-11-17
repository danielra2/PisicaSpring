package mycode.pisicaspring.dtos;

// Folosim 'record' pentru a fi consistent cu PisicaDto și PisicaResponse
public record PisicaNumeVarstaDto(
        String nume,
        int varsta
) {
}