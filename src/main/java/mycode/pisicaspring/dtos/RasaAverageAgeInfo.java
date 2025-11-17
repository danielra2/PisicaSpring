package mycode.pisicaspring.dtos;
import jakarta.validation.constraints.NotBlank;
public record RasaAverageAgeInfo(@NotBlank(message="rasa is required")String rasa, double averageAge){}