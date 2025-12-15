package mycode.pisicaspring.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PisicaResponse (
       @NotNull(message = "id is required")
        Long id,
        @NotBlank(message = "rasa is required")
        String rasa,
        int varsta,
        @NotBlank(message="nume is required")
        String nume
){

}

