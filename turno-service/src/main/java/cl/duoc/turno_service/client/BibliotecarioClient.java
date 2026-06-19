package cl.duoc.turno_service.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import cl.duoc.turno_service.dto.BibliotecarioResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class BibliotecarioClient {
    @Autowired
    private WebClient autorWebClient;

    public BibliotecarioResponse buscarPorId(Long id) {
        log.info("Buscando bibliotecario(a) con id: {}", id);
        try {
            return autorWebClient.get()
                    .uri("/api/v1/bibliotecarios/{id}", id)
                    .retrieve()
                    .bodyToMono(BibliotecarioResponse.class)
                    .block();
        } catch (WebClientResponseException ex) {
            log.error("Error buscando bibiotecario(a) con id {}", id, ex);
            switch (ex.getStatusCode().value()) {
                case 404 -> throw new RuntimeException("No se encontró bibiotecario(a) con id " + id);
                default -> throw new RuntimeException("Error buscando el id " + id, ex);
            }
        }
    }
}