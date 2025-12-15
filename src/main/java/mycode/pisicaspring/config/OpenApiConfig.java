package mycode.pisicaspring.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Pisica CRUD API",
                description = "API pentru gestionarea pisicilor, incluzand operatii de comanda si interogare.",
                version = "1.0.0",
                contact = @Contact(name = "Daniel", email = "contact@example.com"),
                license = @License(name = "MIT", url = "https://opensource.org/licenses/MIT")
        )
)
public class OpenApiConfig {
}
