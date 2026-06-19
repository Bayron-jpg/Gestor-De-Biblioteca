package cl.duoc.turno_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BibliotecarioResponse {
    private Long id;
    private String nombre;
    private String apellido;
    private Integer edad;
}
