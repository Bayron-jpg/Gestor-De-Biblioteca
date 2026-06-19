package cl.duoc.turno_service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Value("${services.bibliotecarios.url}")
    private String bibliotecariosServiceUrl;

    @Bean
    public WebClient bibliotecariosWebClient() {
        return WebClient.builder()
                .baseUrl(bibliotecariosServiceUrl)
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("Accept", "application/json")
                .build();
    }
}
