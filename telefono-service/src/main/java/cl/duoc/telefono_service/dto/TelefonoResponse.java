package cl.duoc.telefono_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TelefonoResponse {
    private Long id;
    private Integer numero;
    private String tipo;
}
