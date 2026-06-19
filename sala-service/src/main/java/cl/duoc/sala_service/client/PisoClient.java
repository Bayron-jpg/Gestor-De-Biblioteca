package cl.duoc.sala_service.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import cl.duoc.sala_service.dto.PisoResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class PisoClient {
    @Autowired
    private WebClient pisoWebClient;

    public PisoResponse buscarPorId(Long id) {
        log.info("Buscando piso con id: {}", id);
        try {
            return pisoWebClient.get()
                    .uri("/api/v1/pisos/{id}", id)
                    .retrieve()
                    .bodyToMono(PisoResponse.class)
                    .block();
        } catch (WebClientResponseException ex) {
            log.error("Error buscando piso con id {}", id, ex);
            switch (ex.getStatusCode().value()) {
                case 404 -> throw new RuntimeException("No se encontró piso con id " + id);
                default -> throw new RuntimeException("Error buscando el id " + id, ex);
            }
        }
    }
}