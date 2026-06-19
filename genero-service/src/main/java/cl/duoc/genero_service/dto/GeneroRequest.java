package cl.duoc.genero_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GeneroRequest {
    @NotBlank(message = "El nombre del género no puede estar vacío.")
    @Size(max = 60, message = "El largo máximo es de 60 caracteres.")
    private String nombre;

    @NotBlank(message = "El nombre del autor no puede estar vacío.")
    @Size(max = 120, message = "El largo máximo es de 120 caracteres.")
    private String descripcion;
}