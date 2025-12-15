package mycode.pisicaspring.repository;

import mycode.pisicaspring.dtos.RasaAverageAgeInfo;
import mycode.pisicaspring.models.Pisica;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PisicaRepository extends JpaRepository<Pisica,Long> {

    Optional<Pisica> findByNumeAndRasa(String nume, String rasa);

    Optional<Pisica> findByNume(String nume);

    Page<Pisica> findByVarstaGreaterThan(int varstaMinima, Pageable pageable);

    Page<Pisica> findByVarstaBetween(int varstaMin, int varstaMax, Pageable pageable);

    Page<Pisica> findByRasaOrderByVarstaAsc(String rasa, Pageable pageable);

    List<Pisica> findTop3ByOrderByVarstaAsc();

    Page<Pisica> findByNumeStartingWith(String nume, Pageable pageable);

    Page<Pisica> findByVarsta(int varsta, Pageable pageable);

    List<Pisica> findTop5ByOrderByVarstaDesc();

    @Query(value = "SELECT DISTINCT p.rasa FROM Pisica p", countQuery = "SELECT COUNT(DISTINCT p.rasa) FROM Pisica p")
    Page<String> findDistinctRase(Pageable pageable);

    @Query(value = "SELECT new mycode.pisicaspring.dtos.RasaAverageAgeInfo(p.rasa, AVG(p.varsta)) FROM Pisica p GROUP BY p.rasa",
            countQuery = "SELECT COUNT(DISTINCT p.rasa) FROM Pisica p")
    Page<RasaAverageAgeInfo> findRasaAverageAge(Pageable pageable);

}
