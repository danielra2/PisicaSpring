package mycode.pisicaspring.dtos;

import jakarta.validation.constraints.NotBlank;

public record PisicaRasaInfo(
        @NotBlank(message = "rasa is required")
        String rasa
) {
}