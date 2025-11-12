package mycode.pisicaspring.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name="pisica")
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class Pisica {
    @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

@Column(nullable = false,length = 255)
    private String rasa;

@Column(nullable = false)
    private int varsta;

@Column(nullable = false,length = 255)
    private String nume;

}
