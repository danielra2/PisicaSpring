package mycode.pisicaspring.repository;

import mycode.pisicaspring.models.Pisica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PisicaRepository extends JpaRepository<Pisica,Long> {
    @Query("select p from Pisica p")
    List<Pisica> getAllPisica();
    Optional<Pisica> findByNumeAndRasa(String nume, String rasa);

}
