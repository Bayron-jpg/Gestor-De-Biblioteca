package cl.duoc.sala_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SalaRequest {
    @NotBlank(message = "El título no puede estar vacío.")
    @Size(max = 40, message = "El largo máximo es de 40 caracteres.")
    private String nombre;
    
    @NotNull(message = "La capacidad no puede estar vacía.")
    private Integer capacidad;

    @NotNull(message = "El ID del piso no puede estar vacío.")
    private Long idPiso;
}