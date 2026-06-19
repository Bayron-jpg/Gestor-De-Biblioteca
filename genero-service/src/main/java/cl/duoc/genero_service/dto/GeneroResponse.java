package cl.duoc.genero_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GeneroResponse {
    private Long id;
    private String nombre;
    private String descripcion;
}
