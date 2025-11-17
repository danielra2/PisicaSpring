package mycode.pisicaspring.dtos;

import jakarta.validation.constraints.NotBlank;

public record PisicaNumeVarstaInfo(
        @NotBlank(message = "nume is required")
        String nume,
        int varsta
) {}