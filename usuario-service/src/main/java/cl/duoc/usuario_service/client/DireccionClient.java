package cl.duoc.usuario_service.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import cl.duoc.usuario_service.dto.DireccionResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class DireccionClient {
    @Autowired
    private WebClient direccionWebClient;

    public DireccionResponse buscarPorId(Long id){
        log.info("Buscando direccion con id: {}", id);
        try {
            return direccionWebClient.get()
                    .uri("/api/v1/direcciones/{id}", id)
                    .retrieve()
                    .bodyToMono(DireccionResponse.class)
                    .block();
        } catch (WebClientResponseException ex) {
            log.error("Error buscando direccion con id {}", id, ex);
            switch (ex.getStatusCode().value()) {
                case 404 -> throw new RuntimeException("No se encontró direccion con id " + id);
                default -> throw new RuntimeException("Error buscando el id " + id, ex);
            }
        }
    }
}