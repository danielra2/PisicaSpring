package mycode.pisicaspring.dtos;

import jakarta.validation.constraints.NotBlank;

public record PisicaResponse (
       @NotBlank(message = "id is required")
        Long id,
        @NotBlank(message = "rasa is required")
        String rasa,
        int varsta,
        @NotBlank(message="nume is required")
        String nume
){

}


