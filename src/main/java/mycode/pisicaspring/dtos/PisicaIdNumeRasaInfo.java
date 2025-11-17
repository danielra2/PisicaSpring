package mycode.pisicaspring.dtos;
import jakarta.validation.constraints.NotBlank;
public record PisicaIdNumeRasaInfo(Long id,
                                   @NotBlank(message="nume is required")
                                   String nume, @NotBlank(message="rasa is required")
                                   String rasa){

}