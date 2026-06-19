package cl.duoc.libro_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Géneros")
                        .version("1.0")
                        .description("Documentación de la API para el microservicio de generos.")
                        .contact(new Contact()
                                .name("Bayron")));
    }
}
