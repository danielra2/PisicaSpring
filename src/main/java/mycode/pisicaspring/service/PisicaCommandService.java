package mycode.pisicaspring.service;

import mycode.pisicaspring.dtos.PisicaDto;
import mycode.pisicaspring.dtos.PisicaResponse;

public interface PisicaCommandService {
    PisicaResponse createPisica(PisicaDto pisicaDto);
}
