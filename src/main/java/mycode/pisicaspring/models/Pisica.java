package mycode.pisicaspring.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name="pisica")
@AllArgsConstructor
@NoArgsConstructor
// Am eliminat @Data și @ToString pentru a evita erorile de compilare.
public class Pisica {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,length = 255)
    private String rasa;

    @Column(nullable = false)
    private int varsta;

    @Column(nullable = false,length = 255)
    private String nume;


    public Long getId() {
        return id;
    }

    public String getRasa() {
        return rasa;
    }

    public int getVarsta() {
        return varsta;
    }

    public String getNume() {
        return nume;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setRasa(String rasa) {
        this.rasa = rasa;
    }

    public void setVarsta(int varsta) {
        this.varsta = varsta;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    @Override
    public String toString() {
        return "Pisica{" +
                "id=" + id +
                ", rasa='" + rasa + '\'' +
                ", varsta=" + varsta +
                ", nume='" + nume + '\'' +
                '}';
    }
}