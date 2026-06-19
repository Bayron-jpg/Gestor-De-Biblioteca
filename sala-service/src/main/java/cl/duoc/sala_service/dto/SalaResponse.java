package cl.duoc.sala_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SalaResponse {
    private Long id;
    private String nombre;
    private Integer capacidad;
    private PisoResponse piso;
}