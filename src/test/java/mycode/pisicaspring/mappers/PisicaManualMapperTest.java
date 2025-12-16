package mycode.pisicaspring.mappers;

import mycode.pisicaspring.dtos.PisicaDto;
import mycode.pisicaspring.dtos.PisicaNumeVarstaDto;
import mycode.pisicaspring.dtos.PisicaRasaInfo;
import mycode.pisicaspring.models.Pisica;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PisicaManualMapperTest {

    private final PisicaManualMapper mapper = new PisicaManualMapper();

    @Test
    void updatePisicaFromDtoCopiesSanitizedValues() {
        PisicaDto dto = new PisicaDto("  Siamese  ", 3, "  Luna  ");
        Pisica target = new Pisica();

        mapper.updatePisicaFromDto(dto, target);

        assertThat(target.getRasa()).isEqualTo("Siamese");
        assertThat(target.getVarsta()).isEqualTo(3);
        assertThat(target.getNume()).isEqualTo("Luna");
    }

    @Test
    void mapPisicaListToPisicaNumeVarstaDtoListFiltersNulls() {
        Pisica valid = new Pisica();
        valid.setNume("  Cleo  ");
        valid.setVarsta(5);

        List<PisicaNumeVarstaDto> dtos = mapper.mapPisicaListToPisicaNumeVarstaDtoList(Arrays.asList(valid, null));

        assertThat(dtos)
                .hasSize(1)
                .first()
                .extracting(PisicaNumeVarstaDto::nume, PisicaNumeVarstaDto::varsta)
                .containsExactly("Cleo", 5);
    }

    @Test
    void mapRasaListToRasaInfoListHandlesNullSource() {
        assertThat(mapper.mapRasaListToRasaInfoList(null)).isEmpty();

        List<PisicaRasaInfo> infos = mapper.mapRasaListToRasaInfoList(Arrays.asList("  British ", null, "British"));
        assertThat(infos).containsExactly(new PisicaRasaInfo("British"));
    }
}
