package cl.duoc.usuario_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TelefonoResponse {
    private Long id;
    private Integer numero;
    private String tipo;
}