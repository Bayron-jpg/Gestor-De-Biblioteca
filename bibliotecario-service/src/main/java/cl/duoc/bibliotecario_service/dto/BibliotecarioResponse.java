package cl.duoc.bibliotecario_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BibliotecarioResponse {
    private Long id;
    private String nombre;
    private String apellido;
    private Integer edad;
}