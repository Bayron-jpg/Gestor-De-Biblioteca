package cl.duoc.usuario_service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Value("${services.telefonos.url}")
    private String telefonoServiceUrl;

    @Value("${services.direcciones.url}")
    private String direccionServiceUrl;

    @Bean
    public WebClient telefonoWebClient() {
        return WebClient.builder()
                .baseUrl(telefonoServiceUrl)
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("Accept", "application/json")
                .build();
    }

    @Bean
    public WebClient direccionWebClient() {
        return WebClient.builder()
                .baseUrl(direccionServiceUrl)
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("Accept", "application/json")
                .build();
    }
}