package cl.duoc.sala_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PisoResponse {
    private Long id;
    private String numero;
    private String descripcion;
}