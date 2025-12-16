package mycode.pisicaspring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class PisicaSpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(PisicaSpringApplication.class, args);
    }


}
