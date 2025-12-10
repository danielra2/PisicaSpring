package mycode.pisicaspring.dtos;

import java.util.List;

public record PisicaListResponsePageable(
        List<PisicaResponse> pisicaDtoList,
        long totalElements,
        int totalPages,
        int PageNumber,
        int PageSize
) {
}
