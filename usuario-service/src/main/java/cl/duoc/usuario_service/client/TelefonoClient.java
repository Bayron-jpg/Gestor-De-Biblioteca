package cl.duoc.usuario_service.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import cl.duoc.usuario_service.dto.TelefonoResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TelefonoClient {
    @Autowired
    private WebClient telefonoWebClient;

    public TelefonoResponse buscarPorId(Long id) {
        log.info("Buscando telefono con id: {}", id);
        try {
            return telefonoWebClient.get()
                    .uri("/api/v1/telefonos/{id}", id)
                    .retrieve()
                    .bodyToMono(TelefonoResponse.class)
                    .block();
        } catch (WebClientResponseException ex) {
            log.error("Error buscando telefono con id {}", id, ex);
            switch (ex.getStatusCode().value()) {
                case 404 -> throw new RuntimeException("No se encontró telefono con id " + id);
                default -> throw new RuntimeException("Error buscando el id " + id, ex);
            }
        }
    }
}