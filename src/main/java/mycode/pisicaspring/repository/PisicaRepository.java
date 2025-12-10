package mycode.pisicaspring.repository;

import mycode.pisicaspring.dtos.PisicaNumeVarstaDto;
import mycode.pisicaspring.dtos.RasaAverageAgeInfo;
import mycode.pisicaspring.models.Pisica;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PisicaRepository extends JpaRepository<Pisica,Long> {
    @Query("select p from Pisica p")
    List<Pisica> getAllPisica();

    Page<Pisica> findAll(Pageable pageable);

    Optional<Pisica> findByNumeAndRasa(String nume, String rasa);

    @Query("SELECT new mycode.pisicaspring.dtos.PisicaNumeVarstaDto(p.nume, p.varsta) FROM Pisica p WHERE p.varsta > :varstaMinima")
    List<PisicaNumeVarstaDto> findPisicasByVarstaGreaterThan(int varstaMinima);

    Optional<Pisica>findByNume(String nume);
    List<Pisica> findByVarstaBetween(int varstaMin, int varstaMax);
    List<Pisica>findAllByOrderByVarstaAsc();
    List<Pisica> findByRasaOrderByVarstaAsc(String rasa);
    List<Pisica> findTop3ByOrderByVarstaAsc();
    List<Pisica> findByNumeStartingWith(String nume);
    List<Pisica> findByVarsta(int varsta);
    List<Pisica> findTop5ByOrderByVarstaDesc();
    @Query("SELECT new mycode.pisicaspring.dtos.RasaAverageAgeInfo(p.rasa, AVG(p.varsta)) FROM Pisica p GROUP BY p.rasa")
    List<RasaAverageAgeInfo> findRasaAverageAge();

}
